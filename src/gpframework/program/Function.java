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

import gpframework.algorithms.components.Mutation;
import gpframework.common.Utils;
import gpframework.common.exceptions.ArityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class representing a function (i.e. non-terminal node) in the syntax
 * tree.
 */
public abstract class Function extends Term 
{    
    /**
     * Function operands.
     */
    protected List<Term> descendants;
    
    /**
     * Constructor.
     */
    public Function() 
    {
        // Does nothing
        descendants = new ArrayList<Term>();
    }
    
    /**
     * Basic constructor for all the functions.
     * 
     * @param descendants descendants of this function
     * @throws ArityException if the number of descendants is wrong
     */
    public Function(List<Term> descendants) throws ArityException 
    {
        this();
        if (descendants.size() != arity())
            throw new ArityException("Wrong number of arguments ("+descendants.size()+") for " + functionName() + " required " + arity());
        for (Term d : descendants) 
            addDescendant(d);  
    }
    
    /**
     * Basic constructor for all the functions.
     * 
     * @param descendants descendants of this function
     * @throws ArityException if the number of descendants is wrong
     */
    public Function(Term[] descendants) throws ArityException 
    {
        this();
        if (descendants.length != arity())
            throw new ArityException("Wrong number of arguments for " + functionName());
        for (Term d : descendants) 
            addDescendant(d);  
    }

    /**
     * Arity of the function.
     * 
     * @return the number of arguments of this function
     */
    public abstract int arity();
    
    
    /**
     * Name of the function.
     * 
     * @return the pretty-printable name of the function (def. class name)
     */
    private String functionName() 
    {
        return this.getClass().getSimpleName();
    }

    
    @Override
    public List<Terminal> getTerminals() 
    {
        List<Terminal> ts = new ArrayList<Terminal>();        
        for (Term d : descendants)
            if (d != null)
                ts.addAll(d.getTerminals());
        Collections.shuffle(ts, Utils.random);
        return ts;
    }

    @Override
    public List<Function> getFunctions() 
    {
        List<Function> fs = new ArrayList<Function>();
        fs.add(this);
        for(Term d : descendants)
            if (d != null)
                fs.addAll(d.getFunctions());
        Collections.shuffle(fs, Utils.random);
        return fs;
    }

    /**
     * Convenient method to add a descendant and update its parent.
     * 
     * @param d descendant to add to this function
     */
    protected void addDescendant(Term d) 
    {
        d.setParent(this,descendants.size());
        descendants.add(d);
    }
    
    /**
     * Convenient method to set a descendant and update its parent.
     * 
     * @param i position of the descendant
     * @param d new descendant
     */
    public void setDescendant(int i, Term d) 
    {
        if (i > descendants.size()-1)
            descendants.add(i, d);
        else
            descendants.set(i, d);
        d.setParent(this, i);
    }
    
    /**
     * Get a descendant by index.
     * 
     * @param i index of the descendant
     * @return the descendant at position i
     */
    public Term getDescendant(int i)
    {
        return descendants.get(i);
    }
    
    /**
     * Replaces a descendant and resets its parent.
     * 
     * @param oldDescendant descendant to be replaced
     * @param newDescendant new descendant
     */
    public void replaceDescendant(Term oldDescendant, Term newDescendant)
    {                            
        int descendantIndex = descendants.indexOf(oldDescendant);
        descendants.set(descendantIndex,newDescendant);
        newDescendant.setParent(this,descendantIndex);
    }
    
    @Override
    public String toString()
    {
        String s = functionName()+"(";
        
        for(int i = 0; i < descendants.size(); i++) {
            s += descendants.get(i);
            if (i != descendants.size()-1)
                s += ",";
        }
        
        return s + ")";
    }

    @Override
    public void applyMutation(Mutation m) 
    {
        m.apply(this);
    }
    
    @Override
    public int complexity()
    {
        int length = 1;
        for(Term d : descendants)
            length += d.complexity();
        
        return length;
    }
}
