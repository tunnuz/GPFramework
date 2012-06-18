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

package gpframework.program.sorting;

import gpframework.common.Utils;
import gpframework.program.Element;
import gpframework.program.NullElement;
import gpframework.program.Terminal;
import gpframework.program.TerminalFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory to generate terminals for the sorting problem.
 */
public class SortingElementFactory extends TerminalFactory {

    /**
     * Constructor.
     * 
     * @param numLabels number of labels
     */
    public SortingElementFactory(int numLabels)
    {
        super(numLabels);
    }
    
    @Override
    public Terminal generate() 
    {
        int label = Utils.random.nextInt(getNumLabels());
        return new Element(label);
    }

    @Override
    public List<Terminal> generateAll() 
    {
        List<Terminal> terminals = new ArrayList<Terminal>(getNumLabels());
        for (int i = 0; i < getNumLabels(); i++)
            terminals.add(new Element(i));
        return terminals;
    }

    @Override
    public Element generateNull() 
    {
        return new NullElement();
    }
    
}
