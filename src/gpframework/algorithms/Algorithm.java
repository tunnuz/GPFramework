/*  Copyright (c) 2012 Tommaso Urli, Markus Wagner
 * 
 *  Tommaso Urli    tommaso.urli@uniud.it   University of Udine
 *  Markus Wagner   wagner@acrocon.com      University of Adelaide
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package gpframework.algorithms;

import gpframework.algorithms.components.Mutation;
import gpframework.algorithms.components.MutationFactory;
import gpframework.algorithms.components.Population;
import gpframework.algorithms.components.Selection;
import gpframework.common.Configurable;
import gpframework.common.Utils;
import gpframework.common.exceptions.ArityException;
import gpframework.common.exceptions.MaximumEvaluationsExceeded;
import gpframework.common.exceptions.ParameterException;
import gpframework.problems.Problem;
import gpframework.program.Program;
import java.util.HashMap;
import java.util.Map;

/**
 * Algorithm whose aim is to optimize the structure of the program. Evolves a
 * population until the optimum for the fitness value is found.
 */
public class Algorithm extends Configurable 
{    
    /** Statistics which we would like to collect at the end of the execution */
    protected Map<String, Object> statistics = new HashMap<String, Object>();
    
    /** The problem we're currently trying to optimize. */
    protected Problem problem;
    
    /** The current solution set. */
    protected Population population;
    
    /** Factory used to generate mutations. */
    protected MutationFactory mutationFactory;
    
    /** Selection criterion to define the new generation. */
    protected Selection selectionCriterion;
    
    /** Size of the evolving population of programs. */
    protected int populationSize;
    
    /** Number of generations executed. */
    protected long generation;
    
    /** Time in which the algorithm started. */
    protected long timeStarted;
    
    /** Maximum number of evaluations available. */
    protected long evaluationsBudget;
    
    /** Maximum seconds available. */
    protected long timeBudget;
    
    /** Maximum population size obtained during optimization.  */
    protected long maximumPopulationSize;
    
    /** Maximum program complexity obtained during optimization.  */
    protected long maximumProgramComplexity;
    
    /**
     * Default constructor
     * 
     * @param programFactory factory used to build the initial population
     * @param mutationFactory factory used to generate mutations
     */
    public Algorithm(MutationFactory mutationFactory, Selection selectionCriterion)
    {
        this.mutationFactory = mutationFactory;
        this.selectionCriterion = selectionCriterion;
    }
    
    /** Main method of the algorithm, initializes the algorithm and calls run().
     * 
     * @param problem problem to solve
     * @throws ParameterException if needed parameters are not in place 
     */
    public final Program solve(Problem problem) throws ParameterException
    {
        // Check if required parameters are in place
        if (!hasParameter("evaluationsBudget") || !hasParameter("timeBudget"))
            throw new ParameterException("Parameters evaluationsBudget and timeBudget are mandatory!");
        
        // Get parameters to bind runtime
        evaluationsBudget = getIntegerParameter("evaluationsBudget");
        timeBudget = getIntegerParameter("timeBudget") * 1000; // convert to milliseconds
        
        // Initialize problem
        initialize(problem);
        
        // Run algorithm
        timeStarted = System.currentTimeMillis();
        Program solution = run(problem);
        
        return solution;
    }

    /**
     * Initialize execution.
     * 
     * @param problem 
     * @throws ParameterException if needed parameters are not in place
     */
    protected void initialize(Problem problem) throws ParameterException 
    {            
        // Set population size
        if (!hasParameter("populationSize"))
            throw new ParameterException("Parameter populationSize is mandatory!");
        populationSize = getIntegerParameter("populationSize");
        
        // Get a random solution set
        population = new Population(problem);
        getPopulation().initialize(populationSize);
                
        // Setup the selection criterion using the fitness function
        selectionCriterion.setFitnessFunction(problem.getFitnessFunction(), evaluationsBudget);  
        
        // Set reference to the problem
        this.problem = problem;
        
        // Reset measures
        this.generation = 0;
        this.maximumPopulationSize = 0;
        this.maximumProgramComplexity = 0;
    
    }

    /**
     * Main method, updates budgets and statistics, override with care.
     * 
     * @param problem problem to solve
     * @return solution 
     */
    protected Program run(Problem problem)
    {        
        statistics.put("evaluationsBudgetExhausted", false);
        statistics.put("timeBudgetExhausted", false);
        
        while((System.currentTimeMillis() - timeStarted) < timeBudget)
        {     
            // Increase generation counter
            generation++;
            
            try {
                // Evolve one generation
                generation();
                
                // Select best program
                Program best = selectionCriterion.getBestIndividual(population);

                // Stops generations if evolved program is optimal
                if (selectionCriterion.isOptimal(best))
                {
                    statistics.put("timeToOptimum", System.currentTimeMillis() - timeStarted);
                    statistics.put("evaluationsUsedToOptimum", evaluationsBudget - selectionCriterion.evaluationsLeft());
                    statistics.put("maxProgramComplexityToOptimum", maximumProgramComplexity);
                    break;
                }
            
            } catch (MaximumEvaluationsExceeded ex) {
                statistics.put("evaluationsBudgetExhausted", true);
                Utils.debug("Maximum iterations exceeded!");
                break;
            }               
        }
        
        if ((System.currentTimeMillis() - timeStarted) >= timeBudget) 
        {
            statistics.put("timeBudgetExhausted", true);
            Utils.debug("Time budget exhausted!");
        }
        
        statistics.put("evaluationsUsed", evaluationsBudget - selectionCriterion.evaluationsLeft());
        
        return selectionCriterion.getBestIndividual(getPopulation());
        
    }

    /**
     * Performs a single generation.
     * 
     * @throws MaximumEvaluationsExceeded upon exhaustion of evaluations budget
     */
    protected void generation() throws MaximumEvaluationsExceeded 
    {

        // Clone population to produce set of offprings
        Population offsprings = new Population(getPopulation());           

        // Select mutations, apply them to offspring
        for (Mutation m : mutationFactory.generate(problem))
            offsprings.mutate(m);
        
        // HACK: adds the correct evaluation to the printout (doesn't affect
        // the number of evaluations used, just performs an evaluation in advance)
        selectionCriterion.evaluate(offsprings.getIndividual(0));
        
        // Select offsprings
        getPopulation().select(selectionCriterion, offsprings);
        
        // Update maximum observed program complexity
        maximumProgramComplexity = Math.max(maximumProgramComplexity, population.getMaximumComplexity());   
    }


    /**
     * Reference to the population.
     * @return the current population.
     */
    public Population getPopulation() 
    {
        return population;
    }

    /**
     * A set of collected statistics.
     * @return the statistics.
     */
    public Map<String, Object> getStatistics() 
    {
        return statistics;
    }
}
