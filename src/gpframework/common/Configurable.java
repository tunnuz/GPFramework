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

package gpframework.common;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a component which can be configured through one or more
 * parameters. It provides functions to access, parse and set parameters.
 */
public class Configurable 
{

    /** Map to store named parameters for this component */
    private Map<String, String> parameters = new HashMap<String,String>();
    
    /**
     * Sets the whole set of parameters
     * 
     * @param newParameters the new set of parameters
     */
    public void setParameters(Map<String, String> newParameters) 
    {
        parameters = newParameters;
    }
    
    /**
     * Retrieves the whole set of parameters
     * 
     * @return the whole set of parameters
     */
    public Map<String, String> getParameters() 
    {
        return parameters;
    }
    
    /**
     * Tells if this component has a certain parameter
     * 
     * @param name name of the parameter to look for
     * @return true if the component has a parameter with the specified name
     */
    public boolean hasParameter(String name) 
    {
        return parameters.containsKey(name);
    }
    
    /**
     * Retrieves a certain parameter as a string
     * 
     * @param name name of the parameter
     * @return a string containing the value of the parameter
     */
    public String getParameter(String name) 
    {
        return parameters.get(name);
    }
    
    /**
     * Sets the value of a parameter
     * 
     * @param name name of the parameter
     * @param value (new) value of the parameter
     */
    public void setParameter(String name, Object value) 
    {
        parameters.put(name, value.toString());
    }
    
    /**
     * Parses a parameter as Double
     * 
     * @param name name of the parameter
     * @return a Double representing the value of the parameter
     */
    public Double getDoubleParameter(String name) 
    {
        return Double.parseDouble(getParameter(name));
    }
    
    /**
     * Parses a parameter as Integer
     * 
     * @param name name of the parameter
     * @return an Integer representing the value of the parameter
     */
    public Integer getIntegerParameter(String name) 
    {
        return Integer.parseInt(getParameter(name));
    }
}
