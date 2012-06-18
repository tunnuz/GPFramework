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

import gpframework.common.Utils;
import gpframework.common.exceptions.MaximumEvaluationsExceeded;
import gpframework.common.exceptions.OptimumFoundException;
import gpframework.problems.Problem;
import gpframework.program.Program;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generation of Programs.
 */
public final class Population implements Cloneable {
    
    /** List of individuals */
    private List<Program> individuals;
    
    /** Factory used to generate new individuals */
    protected Problem problem;
    
    protected long maximumComplexity = 0;
    
    /**
     * Constructor.
     * 
     * @param problem problem to which this population is a solution set
     */
    public Population(Problem problem)
    {
        this.problem = problem;
        this.individuals = new ArrayList<Program>();
    }
    
    /**
     * Copy constructor.
     * 
     * @param other population to replicate
     */
    public Population(Population other)
    {
        this(other.problem);
        for (Program p : other.individuals)
            individuals.add(new Program(p));
        
        // Copy complexity
        this.maximumComplexity = other.maximumComplexity;
    }
    
    /**
     * Initialize a population by generating a number of programs according to
     * the ProgramFactory specified in the Problem.
     * 
     * @param n size of the population
     */
    public void initialize(int n)
    {
        individuals = problem.getProgramFactory().generate(n, problem);
        
        // Find most complex program and cache its complexity
        recomputeMaximumComplexity();
    }
    
    /**
     * Apply a mutation to each individual of the population. Destructive method
     * (i.e. original population is not retained).
     * 
     * @param m the mutation to apply
     */
    public void mutate(Mutation m)
    {
        for (Program p : individuals)
            p.applyMutation(m);
        
        // Find most complex program and cache its complexity
        recomputeMaximumComplexity();
    }
    
    /**
     * Recompute complexity of individual with maximum complexity. For stats.
     */
    protected void recomputeMaximumComplexity()
    {
        maximumComplexity = 0;
        for(Program r : individuals)
            if (r.complexity() > maximumComplexity)
                maximumComplexity = r.complexity();        
    }
    
    /**
     * Given two populations (the original one and the offsprings) modifies the
     * original population by performing an element-wise selection of the best
     * individuals.
     * 
     * @param s selection criterion
     * @param mutated mutated population
     * @throws OptimumFoundException when an optimum is found
     * @throws MaximumEvaluationsExceeded when the maximum number of evaluations has been reached
     */
    public void select(Selection s, Population mutated) throws MaximumEvaluationsExceeded
    {
        s.select(this, mutated);
    }
    
    @Override
    public String toString()
    {
        String s = "";
        for(Program i : individuals)
            s += i.toString() + "\n";
        return s;
    }

    /**
     * Retrieves an individual by index.
     * @param i individual index
     * @return the selected individual
     */
    public Program getIndividual(int i) 
    {
        return individuals.get(i);
    }

    /**
     * Sets an individual by index.
     * @param i individual index
     * @param p program to insert at index i
     */
    public void setIndividual(int i, Program p)
    {
        individuals.set(i, p);
        
        // Find most complex program among remaining ones
        recomputeMaximumComplexity();
    }
    
    /**
     * Gets the whole set of individuals.
     * 
     * @return the individuals
     */
    public List<Program> getIndividuals() 
    {
        return individuals;
    }

    /**
     * Describes individuals.
     * 
     * @return a list of descriptions of the current individuals.
     */
    public List<String> getStringIndividuals()
    {
        List<String> stringIndividuals = new ArrayList<String>();
        for(Program p : individuals)
            stringIndividuals.add(p.toString());
        return stringIndividuals;
    }
    
    /**
     * Describes the results of the individuals.
     * 
     * @return a string describing the results of the current individuals. 
     */
    public String parse() 
    {
        String s = "";
        for(Program i : getIndividuals())
            s += "[F: "+ i.getCachedFitness() + ", C: "+ i.complexity()+"] " + problem.getFitnessFunction().normalize(i) + "\n";
        return s;
    }
    
    /**
     * Adds an individual to the population, updates maximum complexity.
     * 
     * @param p individual to add 
     */
    public void addIndividual(Program p)
    {
        individuals.add(p);
        
        // Update complexity
        if (p.complexity() > maximumComplexity)
            maximumComplexity = p.complexity();
    }
    
    /**
     * Removes an individual from the population, updates maximum complexity.
     * 
     * @param p individual to remove 
     */
    public void removeIndividual(Program p)
    {
        individuals.remove(p);
        
        if (p.complexity() == maximumComplexity)
            // Find most complex program among remaining ones
            recomputeMaximumComplexity();
        
    }
    
    /**
     * Number of individuals.
     * 
     * @return the number of individuals in the population. 
     */
    public int size()
    {
        return individuals.size();
    }
    
    /**
     * Picks a random individual from the population.
     * 
     * @return a random individual from the population.
     */
    public Program getRandomIndividual()
    {
        return individuals.get(Utils.random.nextInt(size()));
    }

    /**
     * Complexity of the most complex individual.
     * 
     * @return the complexity of the most complex individual.
     */
    public long getMaximumComplexity() {
        return maximumComplexity;
    }
}
