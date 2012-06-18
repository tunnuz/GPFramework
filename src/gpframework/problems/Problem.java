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

package gpframework.problems;

import gpframework.common.Configurable;
import gpframework.common.exceptions.ArityException;
import gpframework.indicators.FitnessFunction;
import gpframework.program.*;

/**
 * This class represents a problem instance. Ideally the fitness function and the
 * program factory are sufficient to determine a problem instance (for the problems
 * addressed).
 */
public class Problem extends Configurable
{
    /**
     * Factory used to generate programs (i.e. solutions).
     */
    protected ProgramFactory programFactory;
    
    /**
     * Factory used to evaluate solutions.
     */
    protected FitnessFunction fitnessFunction;
    
    /**
     * Constructor.
     * 
     * @param programFactory factory to generate solutions
     * @param fitnessFunction function to evaluate solutions
     */
    public Problem(ProgramFactory programFactory, FitnessFunction fitnessFunction)
    {
        this.programFactory = programFactory;
        this.fitnessFunction = fitnessFunction;
    }
    
    /**
     * Generate a n-ary function.
     * 
     * @param arity arity of the function
     * @return a n-ary function
     * @throws ArityException if a n-ary function can't be generated
     */
    public Function getFunctionByArity(int arity) throws ArityException 
    {
        return getProgramFactory().getFunctionFactory().generate(arity);
    }

    /**
     * Generate a terminal.
     * 
     * @return a random terminal
     */
    public Terminal getTerminal() 
    {
        return getProgramFactory().getTerminalFactory().generate();
    }
    
    /**
     * Generate a function.
     * 
     * @return a random function
     */
    public Function getFunction() 
    {
        return getProgramFactory().getFunctionFactory().generate();
    }
    
    /**
     * Generate a program.
     * 
     * @return a random program
     */
    public Program getProgram()
    {
        return getProgramFactory().generate(this);
    }

    /**
     * Retrieves the program factory.
     * 
     * @return the program factory
     */
    public ProgramFactory getProgramFactory() 
    {
        return programFactory;
    }

    /**
     * Retrieves the fitness function.
     * 
     * @return the fitness function
     */
    public FitnessFunction getFitnessFunction() 
    {
        return fitnessFunction;
    }

    /**
     * Generates a null term.
     * 
     * @return a null term.
     */
    public Term getNull() {
        return getProgramFactory().getTerminalFactory().generateNull();
    }
}
