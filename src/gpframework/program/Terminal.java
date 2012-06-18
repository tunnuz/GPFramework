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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a terminal in the syntax tree of the program.
 */
public abstract class Terminal extends Term 
{
    
    /** Label, i.e. value, of the terminal */
    private Integer label;
    
    /**
     * Constructor.
     * 
     * @param label value of the terminal
     */
    public Terminal(Integer label) 
    {
        this.label = label;
    }

    @Override
    public List<Terminal> getTerminals() 
    {
        List<Terminal> ts = new ArrayList<Terminal>();
        ts.add(this);
        Collections.shuffle(ts, Utils.random);

        return ts;
    }

    @Override
    public List<Function> getFunctions() 
    {
        return new ArrayList<Function>();
    }
    
    @Override
    public String toString()
    {
        return getLabel().toString();
    }    

    @Override
    public void applyMutation(Mutation m) 
    {
        m.apply(this);
    }
    
    public int complexity()
    {
        return 1;
    }

    /**
     * @return the label
     */
    public Integer getLabel() 
    {
        return label;
    }
}
