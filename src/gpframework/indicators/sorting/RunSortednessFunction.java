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

package gpframework.indicators.sorting;

import gpframework.program.Program;
import java.util.List;

/**
 * Fitness function seeking to minimize the number of maximal sorted blocks
 * (i.e. 1 is best).
 */
public class RunSortednessFunction extends SortednessFunction
{
    /**
     * Constructor.
     * 
     * @param n problem's input size.
     */
    public RunSortednessFunction(int n)
    {
        super(FunctionType.MINIMIZATION);
        this.n = n;
    }
    
    @Override
    public Comparable evaluate(Program solution) {
        
        List<Integer> result = normalize(solution);    

        if(result.isEmpty())
            return getN()+1;
        if(result.size() == 1)
            return getN();
        
        int fitness = 1;

        for (int i = 0; i<result.size()-1; i++) {
            if (result.get(i) > result.get(i+1))
                fitness++;
        }
        
        fitness += getN() - result.size();
        
        return fitness;
    }

    @Override
    public boolean isOptimal(Program solution, int complexity) 
    {        
        if (complexity == 0 || complexity == 1) return true;
        
        int solutionLength = (complexity+1)/2;
        int optimalFitness = 1 + (getN() - solutionLength); 
        
        return solution.getCachedFitness().compareTo((Comparable)optimalFitness) == 0;
    }
    
}
