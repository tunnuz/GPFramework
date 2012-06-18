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

import java.util.List;

/**
 * Abstract factory to generate terminals.
 * 
 * @author Tommaso Urli <tommaso.urli@uniud.it>
 */
public abstract class TerminalFactory {
    
    private int numLabels;
    
    /**
     * Generic constructor for a terminal factory.
     * 
     * @param labels number of labels to be created
     */
    protected TerminalFactory(int labels)
    {
        numLabels = labels;
    }
    
    /**
     * Generate a terminal.
     * 
     * @return 
     */
    public abstract Terminal generate();    
    
    /**
     * Generate all terminals.
     * 
     * @return 
     */
    public abstract List<Terminal> generateAll();

    /**
     * Generate a null terminal.
     * 
     * @return a new Null terminal
     */
    public abstract Terminal generateNull();

    /**
     * Retrieves the total number of different terminals for this factory.
     * 
     * @return the number of different terminals
     */
    public int getNumLabels() 
    {
        return numLabels;
    }

    /**
     * Sets the total number of different terminals.
     */
    public void setNumLabels(int numLabels) 
    {
        this.numLabels = numLabels;
    }
}
