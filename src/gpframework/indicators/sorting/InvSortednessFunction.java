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
 * Fitness function seeking to maximize the number of correctly ordered pairs.
 */
public class InvSortednessFunction extends SortednessFunction 
{    
    /**
     * Constructor.
     */
    public InvSortednessFunction(int n)
    {
        super(FunctionType.MAXIMIZATION);
        this.n = n;
    }
    
    @Override
    public Comparable evaluate(Program solution) {
        // Parse program
        List<Integer> result = normalize(solution);    
        
        double fitness = 0, size = result.size();
        
        if (size == 1)
            return 0.5;        
        
        for (int i = 0; i<size; i++)
            for (int j = i+1; j<size; j++)
                if (result.get(i)<result.get(j)) fitness++;
        
        return fitness;
    }

    @Override
    public boolean isOptimal(Program solution, int complexity) 
    {
        if (complexity == 0 || complexity == 1) return true;
        
        double solutionLength = (complexity+1)/2;
        return solution.getCachedFitness().compareTo((Comparable) ((solutionLength-1) * (solutionLength / 2.0))) == 0;
    }
    
}
