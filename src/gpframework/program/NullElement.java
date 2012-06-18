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
import java.util.ArrayList;

/**
 * Null element in generic list.
 */
public class NullElement extends Element 
{   
    public NullElement()
    {
        super(null, 0);
    }
    
    public ArrayList<Element> parse() 
    {
        return new ArrayList<Element>();
    }

    @Override
    public Object clone() 
    {
        NullElement clone = new NullElement();
        clone.program = program;
        clone.factory = factory;
        return clone;
    }
    
    @Override
    public String toString()
    {
        return "null";
    }
    
    @Override
    public boolean isNull()
    {
        return true;
    }
    
    @Override
    public void applyMutation(Mutation m)
    {
        m.apply(this);
    }
    
    @Override
    public int complexity()
    {
        return 0;
    }
}
