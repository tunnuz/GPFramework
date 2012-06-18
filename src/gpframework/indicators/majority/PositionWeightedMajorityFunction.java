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

import gpframework.indicators.BitArray;
import gpframework.program.Element;
import gpframework.program.Program;
import java.util.List;

/**
 * Variant of MAJORITY fitness function where weights have values w_i = 2^{n-i}.
 */
public class PositionWeightedMajorityFunction extends MajorityFunction
{    
    /**
     * Constructor.
     * 
     * @param n input size of the problem. 
     */
    public PositionWeightedMajorityFunction(int n)
    {
        super(FunctionType.MAXIMIZATION);
        this.n = n;
    }
    
    /**
     * Returns a comparable bit array representing the fitness value.
     * 
     * @param solution program to evaluate
     * @return bit array representing fitness value.
     */
    @Override
    public Comparable evaluate(Program solution)
    {
        List<Element> result = normalize(solution);
                
        BitArray fitness = new BitArray(n);
        for (Element e : result)
            fitness.bits[n-e.getLabel()] = 1;
        
        return fitness;
    }    
        
    /**
     * Checks if solution is optimal by generating a 1-bit array of size n
     * and comparing it with the fitness of the solution.
     * 
     * @param solution
     * @param complexity
     * @return true if solution is optimal.
     */
    @Override
    public boolean isOptimal(Program solution, int complexity)
    {
        int solutionLength = (complexity+1)/2;
        if (solution.complexity() < complexity)
            return false;
                
        BitArray optimum = new BitArray(n);
        for(int i = 0; i < Math.min(solutionLength, n); i++)
            optimum.bits[i] = 1;
        
        return solution.getCachedFitness().compareTo(optimum) == 0;
    }

}
