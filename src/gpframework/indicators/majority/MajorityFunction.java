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

package gpframework.indicators.majority;

import gpframework.indicators.FitnessFunction;
import gpframework.program.Element;
import gpframework.program.Program;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * General superclass for MAJORITY fitness function variants.
 */
public abstract class MajorityFunction extends FitnessFunction 
{   
    /**
     * Constructor.
     * 
     * @param type type of this fitness function.
     */
    public MajorityFunction(FunctionType type)
    {
        super(type);
        
    }
    
    /**
     * Normalize the output of the program. Removes variables where negatives
     * outnumber positives, removes duplicates.
     * 
     * @param solution
     * @return value of the solution after removal of
     */
    @Override
    public List<Element> normalize(Program solution)
    {
        List<Element> original = (List<Element>) solution.parse();
        List<Element> normalized = new ArrayList<Element>();
        List<Integer> normalizedValues = new ArrayList<Integer>();
        Map<Integer, Integer> occurrences = new HashMap<Integer,Integer>();
        
        // Count elements balance
        for(Element e : original)
        {
            int label = e.getLabel();
            if(e.getLabel() > 0)
                occurrences.put(label, occurrences.containsKey(label) ? occurrences.get(label) + 1 : 1);
            else
                occurrences.put(-label, occurrences.containsKey(-label) ? occurrences.get(-label) -1 : -1);
        }   
        
        for(Element e : original)
        {
            int label = e.getLabel();
            if (label > 0 && !normalizedValues.contains(label) && occurrences.get(label) >= 0)
            {
                normalized.add(e);
                normalizedValues.add(label);
            }
        }       
        return normalized;
    }
    
}
