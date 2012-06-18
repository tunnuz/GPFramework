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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implements the EXC sortedness measure (number of exchanges required).
 */
public class ExcSortednessFunction extends SortednessFunction 
{    
    /**
     * Constructor. 
     * 
     * @param n problem's input size.
     */
    public ExcSortednessFunction(int n)
    {
        super(FunctionType.MINIMIZATION);
        this.n = n;
    }
    
    @Override
    public Comparable evaluate(Program solution) 
    {
        List<Integer> result = normalize(solution);      
        
        int fitness = 0;
        
        // If the solution is incomplete
        if ( result.size() != getN() ) 
        {
            // One element: no exchange required
            if (result.isEmpty()) {
                return getN()+1;
            }
            
            // One element: no exchange required
            if (result.size()==1) {
                return getN();
            }
            
            // The +1 was added for the PPSN2012 version
            fitness = getN()-result.size() + 1; 

            int[] numbers = new int[result.size()];
            int[] original = new int[result.size()];
            
            for (int i = 0; i<numbers.length; i++) {
                numbers[i] = result.get(i);
                original[i] = result.get(i);
            }
            
            // Sort into ascending order. Important: number go 0..n-1
            Arrays.sort(numbers);
            
            int[] newNumbers = new int[numbers.length];
            
            for (int i=0; i<numbers.length; i++) {
                int target = numbers[i]; //ith number
                
                for (int j=0; j<numbers.length; j++) {
                    if (original[j]==target) {
                        newNumbers[j] = i;
                    }
                }
            }
            
            List<Integer> newList = new ArrayList<Integer>(result.size());
            for (int i = 0; i<newNumbers.length; i++) {
                newList.add(newNumbers[i]);
            }
            result = newList;
        }
        
        
        // Compute the number of cycles
        boolean[] visited = new boolean[result.size()];
        int cycles = 0;

        for (int i = 0; i < result.size(); i++)
        {
            int current = i;
            boolean cycling = false;
            
            while (true)
            {
                if (!cycling && visited[current])
                    break;
            
            
                if (!visited[current])
                {
                    visited[current] = true;
                    current = result.get(current);
                    if (!cycling) cycling = true;
                } else {
                    cycling = false;
                    cycles++;
                    break;
                }
            }
        }
        
        cycles = result.size()-cycles;
        fitness += cycles;
        
        return fitness;
        
    }

    @Override
    public boolean isOptimal(Program solution, int complexity) 
    {
        if (complexity == 0 || complexity == 1) return true;
        
        int solutionLength = (complexity+1)/2;
        int penalty = getN() == solutionLength ? 0 : 1;             // penalty because of missing elements
        int optimalFitness = (getN() - solutionLength) + penalty;   // missing elements
        
        return solution.getCachedFitness().compareTo((Comparable) optimalFitness) == 0;
    }
    
}
