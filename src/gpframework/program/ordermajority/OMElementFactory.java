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

package gpframework.program.ordermajority;

import gpframework.common.Utils;
import gpframework.program.Element;
import gpframework.program.NullElement;
import gpframework.program.Terminal;
import gpframework.program.sorting.SortingElementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A different Element factory, for Order and Majority problems. Generates also
 * negative labels, and adds random weights to Elements.
 */
public class OMElementFactory extends SortingElementFactory
{
    protected List<Integer> weights = new ArrayList<Integer>();
    
    protected List<Integer> fitnessBySize;
    
    public OMElementFactory(int n)
    {
        super(n);
        
        // Generate n random weights
        for(int i = 0; i < n; i++)
            weights.add(Utils.random.nextInt(1000000));
        
        // Produce list of fitnesses by size
        fitnessBySize = new ArrayList<Integer>(weights);
        Collections.sort(fitnessBySize);
        Collections.reverse(fitnessBySize);
        fitnessBySize.add(0,0);
        
        // Compute incremental sum of weights
        for(int i = 1; i < fitnessBySize.size(); i++)
            fitnessBySize.set(i, fitnessBySize.get(i) + fitnessBySize.get(i-1));
    }

    @Override
    public Terminal generate() {
        int label = Utils.random.nextInt(getNumLabels())+1;   
        int weight = weights.get(label-1);
        if (Utils.random.nextBoolean())
            label = -label;
        
        Element nu = new Element(label, weight);
        nu.setFactory(this);
        return nu;
    }

    @Override
    public List<Terminal> generateAll() {
        
        List<Terminal> terminals = new ArrayList<Terminal>();
        for(int i = 1; i < getNumLabels()+1; i++)
        {
            // Add element
            Element nu = new Element(i, weights.get(i-1));
            nu.setFactory(this);
            terminals.add(nu);
            
            // Add element's negation
            nu = new Element(-i, weights.get(i-1));
            nu.setFactory(this);
            terminals.add(nu);
        }
        
        return terminals;
    }
    
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " " + weights;
    }

    
    @Override
    public Element generateNull()
    {
        Element nil = new NullElement();
        nil.setFactory(this);
        return nil;
    }

    /**
     * @return the fitnessBySize
     */
    public Integer getFitnessBySize(int size) {
        return fitnessBySize.get(size);
    }
}
