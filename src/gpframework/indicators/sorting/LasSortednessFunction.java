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
 * Fitness function implementing the maximization of the longest ascending
 * sequence of numbers (i.e. the longest one is n-long and the optimum).
 */
public class LasSortednessFunction extends SortednessFunction 
{    
    /**
     * Constructor.
     * 
     * @param n problem's input size
     */
    public LasSortednessFunction(int n)
    {
        super(FunctionType.MAXIMIZATION);
        this.n = n;
    }

    @Override
    public Comparable evaluate(Program solution) 
    {
        // Based on code based on http://www.algorithmist.com/index.php/Longest_Increasing_Subsequence.c
        int fitness = 0;
        List<Integer> result = normalize(solution);    
        
        //System.out.println(result);
        
        int[] best = new int[result.size()];
        int[] prev = new int[result.size()];
        
        for (int i = 0; i < result.size(); i++ )  
        {
            best[i] = 1;
            prev[i] = i;
        }

        for (int i = 1; i < result.size(); i++ )
            for (int j = 0; j < i; j++ )
                if ( result.get(i) > result.get(j) && best[i] < best[j]+1 ) 
                {
                    best[i] = best[j] + 1;
                    prev[i] = j;   // prev[] is for backtracking the subsequence
                }

        for (int i = 0; i < result.size(); i++ )
            if ( fitness < best[i] )
                fitness = best[i];
            
        return fitness;        
    }

    @Override
    public boolean isOptimal(Program solution, int complexity) 
    {
        if (complexity == 0 || complexity == 1) return true;
        
        int solutionLength = (complexity+1)/2;
        return solution.getCachedFitness().compareTo((Comparable)solutionLength) == 0;
    }
    
}
