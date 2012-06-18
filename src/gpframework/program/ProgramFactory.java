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

package gpframework.program;

import gpframework.problems.Problem;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract factory to generate programs.
 */
public abstract class ProgramFactory 
{
    /**
     * Factory for function terms.
     */
    private FunctionFactory functionFactory;
    
    /**
     * Factory for terminal terms.
     */
    private TerminalFactory terminalFactory;
    
    /**
     * Constructor.
     * 
     * @param functionFactory factory to generate functions
     * @param terminalFactory factory to generate terminals
     */
    public ProgramFactory(FunctionFactory functionFactory, TerminalFactory terminalFactory)
    {
        this.functionFactory = functionFactory;
        this.terminalFactory = terminalFactory;
    }
    
    /**
     * Generate a program.
     *
     * @return 
     */
    public abstract Program generate(Problem problem);
    
    /**
     * Generate a number of programs.
     * 
     * @param n the number of programs to generate
     * @return a list of {@link Program}
     */
    public List<Program> generate(int n, Problem problem)
    {
        List<Program> programs = new ArrayList<Program>(n);
        for (int i = 0; i < n; i++)
            programs.add(generate(problem));
        return programs;
    }

    /**
     * Get the function factory.
     * 
     * @return the functionFactory
     */
    public FunctionFactory getFunctionFactory() 
    {
        return functionFactory;
    }

    /**
     * Get the terminal factory.
     * 
     * @return the terminalFactory
     */
    public TerminalFactory getTerminalFactory() 
    {
        return terminalFactory;
    }
}
