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

package gpframework.program.sorting;

import gpframework.algorithms.components.mutations.Insertion;
import gpframework.problems.Problem;
import gpframework.program.FunctionFactory;
import gpframework.program.Program;
import gpframework.program.ProgramFactory;
import gpframework.program.TerminalFactory;


/**
 * Program factory to generate sorting programs whose output is of length n.
 * @author Tommaso Urli <tommaso.urli@uniud.it>
 */
public class SortingProgramFactory extends ProgramFactory
{

    /**
     * Constructor.
     * 
     * @param functionFactory function factory
     * @param terminalFactory  terminal factory
     */
    public SortingProgramFactory(FunctionFactory functionFactory, TerminalFactory terminalFactory)
    {
        super(functionFactory, terminalFactory);
    }
    
    @Override
    public Program generate(Problem problem) {
        
        int n = getTerminalFactory().getNumLabels();
        
        Program init = new Program(problem.getTerminal());
        
        // Insert elements until the produced list is of complexity n
        while (init.getTerminals().size() < n)
            init.applyMutation(new Insertion(problem));
        
        return init;
    }
    
}
