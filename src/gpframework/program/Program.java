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
import java.io.Serializable;
import java.util.List;

/**
 * A syntax tree representing a program.
 */
public class Program implements Comparable, Serializable
{
    /**
     * Root term of the program.
     */
    protected Term root;
    
    /**
     * Last observed complexity of this program.
     */
    protected Integer complexity;
    
    /**
     * Last observed fitness of this program.
     */
    protected Comparable cachedFitness;
    
    /**
     * Constructor.
     * 
     * @param root root term of the program
     */
    public Program(Term root)
    {
        this.root = root;
        root.setProgram(this);
    }
    
    /**
     * Copy constructor.
     * 
     * @param other 
     */
    public Program(Program other)
    {
        this.root = (Term) other.root.clone();
        
        for( Term t : getTerms()) 
        {
            t.setProgram(this);
        }
        
        this.cachedFitness = other.cachedFitness;
    }
    
    /**
     * Executes the program to get the result.
     * 
     * @return the result
     */
    public Object parse() 
    {
        return root.parse();
    }

    /**
     * Apply a mutation to this program.
     * 
     * @param m mutation to apply
     */
    public void applyMutation(Mutation m) 
    {
        m.apply(this);
        setCachedFitness(null);
        complexity = null;
    }

    /**
     * Gets the root term.
     * 
     * @return the root term of the program
     */
    public Term getRoot() 
    {
        return root;
    }
    
    /**
     * Replaces the root of the program.
     * 
     * @param t the new root
     */
    public void setRoot(Term t)
    {
        root = t;
    }
    
    /**
     * Gets the program's terminals.
     * 
     * @return a list of all the program's terminals
     */
    public List<Terminal> getTerminals()
    {
        return root.getTerminals();
    }
    
    /**
     * Get a terminal from the tree.
     * 
     * @return 
     */
    public Terminal getTerminal()
    {
        return root.getTerminals().get(0);
    }
    
    /**
     * Gets the program's terms.
     * 
     * @return a list of all the program's terms
     */
    public List<Term> getTerms()
    {
        return root.getTerms();
    }
    
    public Term getRandomTerm()
    {
        return root.getRandomTerm();
    }
    
    public Function getRandomFunction()
    {
        return root.getRandomFunction();
    }
    
    public Terminal getRandomTerminal()
    {
        return root.getRandomTerminal();
    }
    
    public boolean isEmpty()
    {
        return root.isNull();
    }
    
    /**
     * Gets the program's functions.
     * 
     * @return a list of all the program's functions
     */
    public List<Function> getFunctions()
    {
        return root.getFunctions();
    }
    
    /**
     * Pretty printable version of the program.
     * 
     * @return a printable version of the program
     */
    @Override
    public String toString()
    {
        return root.toString();
    }

    /**
     * Get the cached fitness of the program.
     * 
     * @return the fitness
     */
    public Comparable getCachedFitness() {
        return cachedFitness;
    }

    /**
     * Sets the cached fitness of the program.
     * 
     * @param fitness the fitness to set
     */
    public void setCachedFitness(Comparable fitness) {
        this.cachedFitness = fitness;
    }

    @Override
    public int compareTo(Object t) {
        
        Program po = (Program)t;
        
        if (po.getCachedFitness() == null)
            return -1;
        else
            return getCachedFitness().compareTo(po.getCachedFitness());
        
    }
    
    /**
     * Calculates the complexity (number of nodes) of a program.
     * 
     * @return the program's complexity
     */
    public int complexity()
    {
        if (complexity == null)
            complexity = root.complexity();
        return complexity;
    }

}
