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
import gpframework.algorithms.components.selections.SMOGPSelection;
import gpframework.common.Utils;
import gpframework.common.exceptions.MaximumEvaluationsExceeded;
import gpframework.common.exceptions.ParameterException;
import gpframework.problems.Problem;
import gpframework.program.Program;

/**
 * Simple Multi-Objective Genetic Programming (SMO-GP) algorithm.
 */
public class SMOGP extends Algorithm 
{
    /**
     * Flag to record if the first optimal solution has been found.
     */
    boolean optimalFitnessFound;
    
    /**
     * Constructor.
     * 
     * @param mutationFactory mutation factory
     */
    public SMOGP(MutationFactory mutationFactory)
    {
        super(mutationFactory, new SMOGPSelection());
    }
    
    /**
     * Constructor.
     * 
     * @param mutationFactory mutation factory
     * @param selectionCriterion selection criterion
     */
    public SMOGP(MutationFactory mutationFactory, Selection selectionCriterion)
    {
        super(mutationFactory, new SMOGPSelection());
    }
    
    
    @Override
    protected void initialize(Problem problem) throws ParameterException 
    {            
        setParameter("populationSize", 1);
        super.initialize(problem);
    }
    
    
    @Override
    protected void generation() throws MaximumEvaluationsExceeded
    {        
        // Pick random individual of the population as offspring
        Population offsprings = new Population(problem);
        offsprings.addIndividual(new Program(population.getRandomIndividual()));
                
        // Select mutations according to factory, apply them to offspring
        for (Mutation m : mutationFactory.generate(problem))
        {
            offsprings.mutate(m);
        }
   
        // HACK: adds the correct evaluation to the printout (doesn't affect
        // the number of evaluations used, just performs an evaluation in advance)
        selectionCriterion.evaluate(offsprings.getIndividual(0));
        
        // Select offsprings
        population.select(selectionCriterion, offsprings);
        
        // Update maximum complexity
        maximumProgramComplexity = Math.max(maximumProgramComplexity, population.getMaximumComplexity());        
    }

    @Override
    protected Program run(Problem problem)
    {
        optimalFitnessFound = false;        
        statistics.put("evaluationsBudgetExhausted", false);
        statistics.put("timeBudgetExhausted", false);
                
        while((System.currentTimeMillis() - timeStarted) < timeBudget)
        {            
            // Increase generation counter
            generation++;
            
            try {
                // Evolve one generation
                generation();
                            
                // Update maximum experienced population size
                maximumPopulationSize = Math.max(maximumPopulationSize, population.size());
                
                if (!optimalFitnessFound)
                {
                    // Check if we have found a maximal fitness point
                    if (hasOptimalFitnessSolution())
                    {
                        Utils.debug("Optimum found!");
                        optimalFitnessFound = true;
                        statistics.put("timeToOptimum", System.currentTimeMillis() - timeStarted);
                        statistics.put("evaluationsUsedToOptimum", evaluationsBudget - selectionCriterion.evaluationsLeft());
                        statistics.put("maxPopulationSizeToOptimum", maximumPopulationSize);
                        statistics.put("maxProgramComplexityToOptimum", maximumProgramComplexity);
                    }
                    
                } else {
                    // Check if we have found a pareto optimal front
                    
                    //Utils.debug("Population: \n" + population.parse());
                    
                    if (hasCompleteFront())
                    {
                        Utils.debug("Complete Pareto front found!");
                        statistics.put("timeToCompleteFront", System.currentTimeMillis() - timeStarted);
                        statistics.put("evaluationsUsedToCompleteFront", evaluationsBudget - selectionCriterion.evaluationsLeft());
                        statistics.put("maxPopulationSizeToCompleteFront", maximumPopulationSize);
                        statistics.put("maxProgramComplexityToCompleteFront", maximumProgramComplexity);
                        break;
                    }
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
            Utils.debug("Time budget exhausted! Time used: " + (System.currentTimeMillis() - timeStarted) + " time budget: " + timeBudget);
        }
        
        statistics.put("evaluationsUsed", evaluationsBudget - selectionCriterion.evaluationsLeft());

        return selectionCriterion.getBestIndividual(getPopulation());
        
    }

    /**
     * Checks if the Pareto front is complete.
     * @return true if all the fitness/complexity trade-offs have been found
     */
    private boolean hasCompleteFront() {
                
        // Input size
        int n = selectionCriterion.getFitnessFunction().getN();
                
        // If population hasn't got the correct size return false
        if (population.size() != n+1)
            return false;
        
        // If a program is not pareto optimal return false
        for(Program p : population.getIndividuals())
            if (!selectionCriterion.isOptimal(p, p.complexity()))
                return false;
        return true;
        
    }

    /**
     * Checks if the best individual is optimal
     */
    private boolean hasOptimalFitnessSolution() 
    {    
        return selectionCriterion.isOptimal(selectionCriterion.getBestIndividual(population));
    }
}
