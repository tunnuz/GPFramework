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

package gpframework.algorithms.components.mutations;

import gpframework.algorithms.components.Mutation;
import gpframework.common.exceptions.ArityException;
import gpframework.problems.Problem;
import gpframework.program.Function;
import gpframework.program.Program;
import gpframework.program.Terminal;

/** 
 * This mutation selects a random function node in a program tree and replaces 
 * it with another function with the same arity. When applied to terminals,
 * replaces the terminal with another one. 
 */
public class Replacement extends Mutation 
{
    /**
     * Constructor.
     * 
     * @param problem problem linked to this mutation (allows to generate Terms)
     */
    public Replacement(Problem problem)
    {
        super(problem);
    }
    
    @Override
    public void apply(Function f) 
    {           
        // Select n-1-ary function to replace f
        Function n = null;
        try {
            n = problem.getFunctionByArity(f.arity());
            n.setProgram(f.getProgram());
        } catch (ArityException ex) {
            // Fail gently
            System.err.println("Can't find any function with arity " + f.arity() + " to replace " + f);
            return;
        }
                
        // Replace descendants
        for(int i = 0; i < f.arity(); i++)
            n.setDescendant(i, f.getDescendant(i));
        
        // Replace f with new function
        if (f.getParent() != null)
            f.getParent().replaceDescendant(f, n);
        else
            f.getProgram().setRoot(n);
    }
    
    @Override
    public void apply(Terminal t) 
    {                
        // Get random terminal to replace t
        Terminal r = problem.getTerminal();
                
        r.setProgram(t.getProgram());
        
        // Replace t
        if (t.isRoot()) 
            t.getProgram().setRoot(r);
        else
            t.getParent().replaceDescendant(t, r);        
    }

    @Override
    public void apply(Program program) 
    {
        if (program.isEmpty())
            return;
        
        // Can be applied to any term 
        program.getRandomTerm().applyMutation(this);
    }

    @Override
    public Object clone() 
    {
        return new Replacement(problem);
    }
    
}
