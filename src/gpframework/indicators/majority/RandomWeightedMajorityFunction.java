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

package gpframework.indicators.majority;

import gpframework.program.Element;
import gpframework.program.Program;
import gpframework.program.ordermajority.OMElementFactory;
import java.util.List;

/**
 * Variant of MAJORITY fitness function where weights are assigned randomly.
 */
public class RandomWeightedMajorityFunction extends MajorityFunction
{      
    /**
     * Constructor. 
     * 
     * @param n problem input size. 
     */
    public RandomWeightedMajorityFunction(int n)
    {
        super(FunctionType.MAXIMIZATION);
        this.n = n;
    }
    
    @Override
    public Comparable evaluate(Program solution) {
        
        List<Element> result = normalize(solution);
               
        int fitness = 0;
        for (Element e : result) 
            fitness += e.getWeight();
        
        return fitness;
    }

    @Override
    public boolean isOptimal(Program solution, int complexity) 
    {
        int solutionLength = (complexity+1)/2;
        if (solution.complexity() < complexity)
            return false;
                
        // Otherwise check if it's optimal wrt. its complexity
        Element first = ((Element)solution.getTerminal());
        OMElementFactory elementFactory = (OMElementFactory) first.getFactory();
        
        // Compute max fitness with given complexity (because of weighting
        // it is necessary to query the element factory)
        int maxFitness = elementFactory.getFitnessBySize(Math.min(solutionLength, n));
                
        // Return comparison
        return solution.getCachedFitness().compareTo((Comparable) maxFitness) == 0;
    }
}
