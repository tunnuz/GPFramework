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

package gpframework.indicators;

/**
 * Comparable bit array to represent fitness in position-weighted fitness functions.
 */
public class BitArray implements Comparable 
{   
    /**
     * Bits in the fitness value.
     */
    public int[] bits;

    /**
     * Constructor.
     * 
     * @param n length of bit array.
     */
    public BitArray(int n)
    {
        bits = new int[n];
    }
    
    /**
     * Constructor.
     * 
     * @param initialBits initial set of bits.
     */
    public BitArray(int[] initialBits)
    {
        bits = initialBits;
    }
    
    /**
     * Negates set of bits.
     */
    public void negate()
    {
        for(int i = 0; i < bits.length; i++)
            bits[i] = 1 - bits[i];
    }
    
    @Override
    public int compareTo(Object t) {
        
        if (!(t instanceof BitArray))
            throw new UnsupportedOperationException("Can't compare a " + getClass() + " with a " + t.getClass());
        
        BitArray other = (BitArray) t;
        
        if (other.bits.length != this.bits.length)
            throw new UnsupportedOperationException("Can't compare BitArrays of different size!");
        
        if (this == other)
            return 0;
        
        for(int i = 0; i < bits.length; i++)
        {
            if (bits[i] == other.bits[i])
                continue;
            
            if (bits[i] == 1)
                return 1;
            else
                return -1;
        }
        
        return 0;
        
    }
    
    /**
     * Gets array length.
     * 
     * @return length of the bit array.
     */
    public int size()
    {
        return bits.length;
    }
    
    @Override
    public String toString()
    {
        String s = "";
        
        for(int e : bits)
            s += e + " ";
        
        return s;
    }

}
