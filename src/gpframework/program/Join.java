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

import gpframework.common.exceptions.ArityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A join operator (for generic lists).
 */
public class Join extends Function
{
    /**
     * Default constructor.
     */
    public Join()
    {
        super();
    }
    
    /**
     * Forward constructor
     */
    public Join(Term[] descendants) throws ArityException 
    {
        super(descendants);
    }
   
    /**
     * Forward constructor
     */
    public Join(List<Term> descendants) throws ArityException 
    {
        super(descendants);
    }    
    
    @Override
    public List<Element> parse() 
    {
        // Get result of operands
        List<Element> result = (List<Element>) descendants.get(0).parse();
        List<Element> rightResult = (List<Element>) descendants.get(1).parse();

        result.addAll(rightResult);
        
        
        // Merge results by ignoring duplicates (not done here anymore, look into SortednessFunction
        /*
        for (int e : rightResult)
            if (!result.contains(e))
                result.add(e);
        */
        return result;
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Object clone() 
    {
                
        // Recursively clone descendants
        List<Term> clonedDescendants = new ArrayList<Term>();
        for (int i = 0; i < descendants.size(); i++)
            clonedDescendants.add(i, (Term) descendants.get(i).clone());
                
        Term clone = null;
        
        try {   
            clone = new Join(clonedDescendants);
        } catch (ArityException ex) {
            Logger.getLogger(Join.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        clone.setProgram(program);
        return clone;
    }
    
    @Override
    /**
     * Shorter toString (prints J(A,B) instead than Join(A,B))
     */
    public String toString()
    {
        String s = "J(";
        
        for(int i = 0; i < descendants.size(); i++) {
            s += descendants.get(i);
            if (i != descendants.size()-1)
                s += ",";
        }
        
        return s + ")";
    }
}
