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
import gpframework.common.Utils;
import gpframework.problems.Problem;
import gpframework.program.Function;
import gpframework.program.Program;
import gpframework.program.Term;
import gpframework.program.Terminal;

/** 
 * Mutation for adding terminals to the syntactic tree. Uses a function to
 * generate a new branch, and fills it with old and new descendants. 
 */
public class Insertion extends Mutation 
{       
    /**
     * Constructor.
     * 
     * @param problem problem linked to this mutation (allows to generate Terms)
     */
    public Insertion(Problem problem)
    {
        super(problem);
    }

    @Override
    public void apply(Term t) 
    {   
        // Get parent node
        Function parent = t.getParent();
        
        // Select a (random) function to use for insertion
        Function f = problem.getFunction();
        f.setProgram(t.getProgram());

        // Choose random descendant to attach old term
        int tPlace = Utils.random.nextInt(f.arity());
        
        // Fill the function with random descendants
        for (int i = 0; i < f.arity(); i++) {
            if (i != tPlace) 
                // Add random terminal descendants
                f.setDescendant(i, problem.getTerminal());
            else
                // Add mutation point at random position in the new function
                f.setDescendant(i, t);
        }
                
        // Replace old node (or root) with new function
        if (parent != null)
            parent.replaceDescendant(t, f);
        else
            t.getProgram().setRoot(f);
    }

    @Override
    public void apply(Program program) 
    {
        // Handle special case of empty tree, otherwise mutate random term
        if (program.isEmpty())
        {
            Terminal nu = problem.getTerminal();
            nu.setProgram(program);
            program.setRoot(nu);
        }
        else
            program.getRandomTerm().applyMutation(this);
        
    }

    @Override
    public Object clone() 
    {
        return new Insertion(problem);
    }
}
