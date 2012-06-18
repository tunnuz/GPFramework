package gpframework.indicators;

import gpframework.common.exceptions.OptimumFoundException;
import gpframework.program.Program;

/**
 * The base class for fitness indicators.
 * 
 * @author Tommaso Urli <tommaso.urli@uniud.it>
 */
public abstract class FitnessFunction {
    
    protected int n;

    /**
     * @return the n
     */
    public int getN() {
        return n;
    }
    
    /**
     * Type of objective function (minimization or maximization).
     */
    public enum FunctionType
    {
        MINIMIZATION,
        MAXIMIZATION
    }
    
    /**
     * Type of the function.
     */
    protected FunctionType type;
    
    /**
     * Constructor.
     * 
     * @param type type of the function
     */
    public FitnessFunction(FunctionType type)
    {
        this.type = type;
    }
    
    /**
     * Abstract method to evaluate the quality of a solution.
     * 
     * @param solution solution to evaluate
     * @return a comparable fitness value
     * @throws OptimumFoundException when an optimum is found
     */
    public abstract Comparable evaluate(Program solution);
    
    /**
     * Abstract method to normalize a solution before feeding it into the fitness function.
     * 
     * @param solution solution to evaluate
     * @return normalized solution (e.g. for sortedness remove duplicates)
     */
    public Object normalize(Program solution)
    {
        return solution.parse();
    }


    /**
     * Checks if a program is optimal with respect to a given complexity.
     * 
     * @param solution
     * @param complexity
     * @return true if the program's fitness is the best possible wrt. the 
     *         specified complexity
     */
    public abstract boolean isOptimal(Program solution, int complexity);
    

    /**
     * Tells if this program is optimal with respect to the problem.
     * 
     * @param solution
     * @return true fit he program's fitness is the best possible
     */
    public boolean isOptimal(Program solution)
    {        
        // Default behavior, pick n and get optimal complexity with n, then call isOptimal(solution, complexity)
        return isOptimal(solution, (2*n)-1);
    }
    
    /**
     * Fitness function type.
     */
    public FunctionType getType() {
        return type;
    }
}
