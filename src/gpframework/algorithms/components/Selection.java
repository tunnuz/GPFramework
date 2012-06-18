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

package gpframework.algorithms.components;

import gpframework.common.exceptions.MaximumEvaluationsExceeded;
import gpframework.common.exceptions.OptimumFoundException;
import gpframework.indicators.FitnessFunction;
import gpframework.indicators.FitnessFunction.FunctionType;
import gpframework.program.Program;
import java.util.Collections;
import java.util.List;

/**
 * A class which represents a selection criterion to decide which programs must
 * be kept for the next generation and which ones must not.
 */
public abstract class Selection 
{
    /**
     * Fitness function to evaluate a solution's quality.
     */
    protected FitnessFunction fitnessFunction;
    
    /**
     * Maximum number of evaluations.
     */
    protected long evaluationsBudget;
    
    /**
     * Sets the fitness function to use for selection.
     * @param fitnessFunction a fitness function
     * @param evaluationsBudget a maximum number of evaluations
     */
    public void setFitnessFunction(FitnessFunction fitnessFunction, long evaluationsBudget)
    {
        this.fitnessFunction = fitnessFunction;
        this.evaluationsBudget = evaluationsBudget;
    }
    
    public FitnessFunction getFitnessFunction()
    {
        return fitnessFunction;
    }
    
    /**
     * Element-wise selection of new individuals from the mutated population, is
     * supposed to be overridden by more sophisticated subclasses.
     * 
     * @param original original population
     * @param mutated mutated
     * 
     * @throws OptimumFoundException if an optimum is found during evaluation
     * @throws MaximumEvaluationsExceeded if the maximum number of evaluations is reached
     */
    public abstract void select(Population original, Population mutated) throws MaximumEvaluationsExceeded;
    /**
     * Tells which of the two programs is most fit. Normalizes each fitness function
     * into a minimization function.
     * 
     * @param original
     * @param mutated
     * 
     * @return -1 if the first program is better, 1 if the second is better, 0 if they are equal
     */
    protected int compare(Program original, Program mutated) throws MaximumEvaluationsExceeded
    {       
        // Check if there are any evaluations
        if (evaluationsBudget == 0)
            throw new MaximumEvaluationsExceeded();
        
        // Optimization factor, i.e. convert maximization functions into minimization functions
        int optimizationFactor = (fitnessFunction.getType() == FunctionType.MAXIMIZATION ? -1 : 1);
        
        
        return evaluate(original).compareTo(evaluate(mutated)) * optimizationFactor;
    }
    
    /**
     * Evaluates the quality of a program and caches the fitness value.
     * @param solution program to evaluate
     * 
     * @return a fitness function value
     */
    public Comparable evaluate(Program solution)
    {
        // If we don't have a cached fitness we evaluate it, and spend one evaluation
        if (solution.getCachedFitness() == null)
        {
            evaluationsBudget--;
            solution.setCachedFitness(fitnessFunction.evaluate(solution));
        }
        
        return solution.getCachedFitness();
    }

    /**
     * Identifies the best individual in a population.
     * @param population the population to scan
     * @return the best individual
     */
    public Program getBestIndividual(Population population) {

        // Get individuals
        List<Program> individuals = population.getIndividuals();
        
        // Sort them according to fitness (either increasing or decreasing)
        Collections.sort(individuals);

        // Return first or last depending on function type
        if (fitnessFunction.getType() == FunctionType.MAXIMIZATION)
            return individuals.get(individuals.size()-1);
        else
            return individuals.get(0);
    }

    /**
     * Gets the number of evaluations left.
     * 
     * @return the remaining evaluations budget
     */
    public long evaluationsLeft() 
    {
        return evaluationsBudget;
    }
    
    /**
     * Tells if a solution is optimal for the current problem.
     * 
     * @param solution solution to evaluate
     * @return true if the solution is optimal
     */
    public boolean isOptimal(Program solution) 
    {
        return fitnessFunction.isOptimal(solution);
    }
    
    /**
     * Tells if a solution's fitness is the best possible given the specified
     * complexity.
     * 
     * @param solution
     * @param complexity
     * @return true if the solution's fitness is optimal wrt. the specified complexity
     */
    public boolean isOptimal(Program solution, int complexity)
    {
        return fitnessFunction.isOptimal(solution, complexity);
    }
}
