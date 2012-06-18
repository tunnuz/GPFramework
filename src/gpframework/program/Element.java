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

import java.util.ArrayList;
import java.util.List;

/**
 * An element of a generic list.
 */
public class Element extends Terminal 
{
    /**
     * Weight of the element.
     */
    protected Integer weight;

    /**
     * Constructor. 
     * @param n
     * @param w 
     */
    public Element(Integer n, Integer w)
    {
        super(n);
        weight = w;
    }
    
    /**
     * Constructor.
     * @param n label, can be null
     */
    public Element(Integer n)
    {
        this(n,1);
    }

    @Override
    public List<Element> parse() 
    {
        List<Element> singleton = new ArrayList<Element>();
        singleton.add(this);
        return singleton;
    }

    @Override
    public Object clone() 
    {
        Element clone = new Element(getLabel(), getWeight());
        clone.program = program;
        clone.factory = factory;
        return clone;
    }
    
    @Override
    public String toString()
    {
        return getLabel().toString();
    }

    /**
     * Retrieves the weight of this element.
     * 
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }
}
