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

package gpframework.algorithms.components;

import gpframework.problems.Problem;
import gpframework.program.Function;
import gpframework.program.Program;
import gpframework.program.Term;
import gpframework.program.Terminal;

/** 
 * This is the abstract base class for every mutation which can be applied to a
 * program. Although the Program interface tries to be as fault-free as possible,
 * when implementing mutations as extensions of this class make sure that the
 * resulting tree is correct (e.g. parent, program, parentIndex fields are OK).
 */
public abstract class Mutation implements Cloneable
{
    /**
     * Reference to the problem we're trying to solve.
     */
    protected Problem problem;
    
    /**
     * Constructor.
     * 
     * @param problem problem linked to this mutation (allows to generate Terms)
     */
    protected Mutation(Problem problem) 
    {
        this.problem = problem;
    }
    
    /**
     * Apply this mutation to a Function. If this method is not overloaded by 
     * Mutation's subclasses, the apply(Term) method will be called.
     * 
     * @param f function to which this mutation must be applied 
     */
    public void apply(Function f)
    {
        // If there's no specific method to handle functions, handle a Term
        apply((Term)f);
    }
    
    /**
     * Apply this mutation to a Terminal. If this method is not overloaded by 
     * Mutation's subclasses, the apply(Term) method will be called.
     * 
     * @param t terminal to which this mutation must be applied 
     */
    public void apply(Terminal t)
    {
        // If there's no specific method to handle terminals, handle a Term
        apply((Term)t);
    }
    
    /**
     * Generic application of this Mutation to a Term. To be overridden when 
     * a Mutation is able to handle Terms regardless of them being Functions 
     * or Terminals.
     * 
     * @param t term to which this mutation must be applied
     */
    public void apply(Term t)
    {   
        // Skip mutations which don't override this method, i.e. can't be 
        // applid to a generic Term
        return;
    }
    
    /**
     * High-level method to select where the Mutation must be applied in the
     * tree. Internally can call apply(Term/Function/Terminal) depending on which
     * kind of nodes this Mutation is meant to be applied.
     * 
     * @param program program to which this mutation must be applied
     */
    public abstract void apply(Program program);
    
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public abstract Object clone();
}
