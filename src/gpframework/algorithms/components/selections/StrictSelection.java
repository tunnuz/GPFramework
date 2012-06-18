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

/** 
 * This selection chooses an offspring if it's strictly better than its parent.
 */
public class StrictSelection extends Selection 
{
    /**
     * Performs single-program selection.
     * 
     * @param original original program
     * @param mutated offspring
     * @return return the offspring if it's strictly better than the parent 
     * @throws MaximumEvaluationsExceeded upon exhaustion of evaluations budget
     */
    private Program select(Program original, Program mutated) throws MaximumEvaluationsExceeded 
    {
        return compare(original, mutated) > 0 ? mutated : original;
    }
    
    @Override
    public void select(Population original, Population mutated) throws MaximumEvaluationsExceeded 
    {    
        for (int i = 0; i < original.getIndividuals().size(); i++)
            original.setIndividual(i, this.select(original.getIndividual(i), mutated.getIndividual(i)));
    } 
}
