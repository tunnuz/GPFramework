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

package gpframework.algorithms.components.selections;

import gpframework.algorithms.components.Population;
import gpframework.algorithms.components.Selection;
import gpframework.common.exceptions.MaximumEvaluationsExceeded;
import gpframework.program.Program;
import java.util.ArrayList;
import java.util.List;

/** 
 * Multi-objective selection criterion for SMO-GP. First removes all previous
 * individuals which are dominated by offspring, then adds offspring to the
 * population if there is non-dominated by any previous individual. 
 */
public class SMOGPSelection extends Selection
{
    @Override
    public void select(Population original, Population mutated) throws MaximumEvaluationsExceeded 
    {       
        // Mutated population is only 1-sized
        Program o = mutated.getIndividual(0);
        List<Program> weaklyDominated = new ArrayList<Program>();

        // If some solution in the population dominates o, keep the population as is        
        for (Program i : original.getIndividuals())
        {
            if (dominates(i,o)) return;
            if (weaklyDominates(o,i))
                weaklyDominated.add(i);      
        }
        
        // Remove from population solutions which are weakly dominated by o
        original.getIndividuals().removeAll(weaklyDominated);
        
        // Add o to the population
        original.addIndividual(o);
    }
    
    /**
     * Check whether the first program weakly dominates the second one, wrt. 
     * fitness and complexity. That is, let F be the fitness of a program and 
     * C its complexity in terms of number of nodes in the syntax tree, 
     * checks if:
     * 
     *       F(p1) less or equal F(p2) and C(p1) less or equal C(p2) 
     * 
     * @param p1 first program 
     * @param p2 second program
     * @return true if the first program weakly dominates the second one
     * @throws MaximumEvaluationsExceeded upon exhaustion of evaluations budget
     */
    private boolean weaklyDominates(Program p1, Program p2) throws MaximumEvaluationsExceeded
    {
        return ( compare(p1,p2) <= 0 && p1.complexity() <= p2.complexity());
    }
    
    /**
     * Check whether the first program dominates the second one, wrt. fitness 
     * and complexity. That is, let F be the fitness of a program and C its 
     * complexity in terms of number of nodes in the syntax tree, checks if:
     * 
     *   1. p1 weakly dominates p2, and
     *   2. F(p1) strictly less than F(p2) or C(p1) strictly less than C(p2)
     * 
     * @param p1 first program 
     * @param p2 second program
     * @return true if the first program weakly dominates the second one
     * @throws MaximumEvaluationsExceeded upon exhaustion of evaluations budget
     */
    private boolean dominates(Program p1, Program p2) throws MaximumEvaluationsExceeded
    {
        return ( weaklyDominates(p1,p2) && ( compare(p1,p2) < 0 || p1.complexity() < p2.complexity()));
    }
    
}
