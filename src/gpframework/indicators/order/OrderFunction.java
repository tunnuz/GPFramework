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

package gpframework.indicators.order;

import gpframework.indicators.FitnessFunction;
import gpframework.program.Element;
import gpframework.program.Program;
import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for ORDER fitness function variants.
 */
public abstract class OrderFunction extends FitnessFunction 
{
    /**
     * Constructor.
     * 
     * @param type type of fitness function.
     */
    public OrderFunction(FunctionType type)
    {
        super(type);
    }
    
    @Override
    public List<Element> normalize(Program solution)
    {
        List<Element> original = (List<Element>) solution.parse();
        
        List<Element> normalized = new ArrayList<Element>();
        List<Integer> normalizedValues = new ArrayList<Integer>();
        
        
        for(Element e : original)
        {
            if (!normalizedValues.contains(-e.getLabel()) && !normalizedValues.contains(e.getLabel()))
            {
                normalized.add(e);
                normalizedValues.add(e.getLabel());
            }
        }
        
        return normalized;
    }
    
}
