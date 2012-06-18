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

package gpframework;

import gpframework.algorithms.Algorithm;
import gpframework.algorithms.components.MutationFactory;
import gpframework.algorithms.components.Selection;
import gpframework.common.Utils;
import gpframework.common.exceptions.ParameterException;
import gpframework.indicators.FitnessFunction;
import gpframework.problems.Problem;
import gpframework.program.FunctionFactory;
import gpframework.program.Program;
import gpframework.program.ProgramFactory;
import gpframework.program.TerminalFactory;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.cli.*;

/**
 * Executable program which allows to pass various parameters on the command
 * line and run experiments accordingly.
 */
public class RunExperiment 
{        
    public static boolean cluster = false;
    
    /**
     * Application's entry point.
     * 
     * @param args
     * @throws ParseException
     * @throws ParameterException 
     */
    public static void main(String[] args) throws ParseException, ParameterException
    {
        // Failsafe parameters
        if (args.length == 0)
        {
            args = new String[] { 
                "-f", "LasSortednessFunction", 
                "-n", "5" ,
                "-ff", "JoinFactory", 
                "-tf", "SortingElementFactory", 
                "-pf", "SortingProgramFactory",
                "-s", "SMOGPSelection",
                "-a", "SMOGP",
                "-t", "50",
                "-e", "1000000000",
                "-mf", "SingleMutationFactory",
                "-d",
                "-bn", "other" 
            }; 
        }
        
        // Create options
        Options options = new Options();
        setupOptions(options);
        
        // Read options from the command line
        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        
        // Print help if parameter requirements are not met
        try {
            cmd = parser.parse(options, args);
        } 
        
        // If some parameters are missing, print help
        catch (MissingOptionException e)
        {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("java -jar GPFramework \n", options);
            System.out.println();
            System.out.println("Missing parameters: " + e.getMissingOptions());
            return;
        }
                
        // Re-initialize PRNG
        long seed = System.currentTimeMillis();
        Utils.random = new Random(seed);
        
        // Set the problem size
        int problemSize = Integer.parseInt(cmd.getOptionValue("n"));
        
        // Set debug mode and cluster mode
        Utils.debug = cmd.hasOption("d");
        RunExperiment.cluster = cmd.hasOption("c");
        
        // Initialize fitness function and some factories
        FitnessFunction fitnessFunction = fromName(cmd.getOptionValue("f"), problemSize);
        MutationFactory mutationFactory = fromName(cmd.getOptionValue("mf"));
        Selection selectionCriterion = fromName(cmd.getOptionValue("s"));
        FunctionFactory functionFactory = fromName(cmd.getOptionValue("ff"));
        TerminalFactory terminalFactory = fromName(cmd.getOptionValue("tf"), problemSize);
        ProgramFactory programFactory = fromName(cmd.getOptionValue("pf"), functionFactory, terminalFactory);
        
        // Initialize algorithm
        Algorithm algorithm = fromName(cmd.getOptionValue("a"), mutationFactory, selectionCriterion);
        algorithm.setParameter("evaluationsBudget", cmd.getOptionValue("e"));
        algorithm.setParameter("timeBudget", cmd.getOptionValue("t"));
        
        // Initialize problem
        Problem problem = new Problem(programFactory, fitnessFunction);
        Program solution = algorithm.solve(problem);
        
        Utils.debug("Population results: ");
        Utils.debug(algorithm.getPopulation().toString());
        Utils.debug(algorithm.getPopulation().parse());
        
        Map<String, Object> entry = new HashMap<String, Object>();

        // Copy algorithm setup
        for(Object o : options.getRequiredOptions())
        {
            Option option = options.getOption(o.toString());
            entry.put(option.getLongOpt(), cmd.getOptionValue(option.getOpt()));
        }
        entry.put("seed", seed);

        // Copy results
        entry.put("bestProgram", solution.toString());
        entry.put("bestSolution", fitnessFunction.normalize(solution));
        
        // Copy all statistics
        entry.putAll(algorithm.getStatistics());
        
        Utils.debug("Maximum encountered population size: " + algorithm.getStatistics().get("maxPopulationSizeToCompleteFront") );
        Utils.debug("Maximum encountered tree size: " + algorithm.getStatistics().get("maxProgramComplexityToCompleteFront") );
        Utils.debug("Solution complexity: " + solution.complexity() + "/" + (2*problemSize-1));
    }
    	
    /**
     * Generates an object from a parameter value.
     * @param <T> type of the return parameter.
     * @param className name of the subclass to instantiate.
     * @return an instantiated object of type className.
     */
    @SuppressWarnings("unchecked")
    public synchronized static <T> T fromName(String className, Object ... parameters) {

        // Get array of classes (to find right constructor)
        Class[] classes = new Class[parameters.length];
        for(int p = 0; p < parameters.length; p++)
        {
            // Integer must be transformed to int
            if (parameters[p].getClass() == Integer.class)
            {
                classes[p] = int.class;
            }
            else
            {
                // Find superclass
                Class currentClass = parameters[p].getClass();
                while (currentClass.getSuperclass() != Object.class)
                    currentClass = currentClass.getSuperclass();
                
                classes[p] = currentClass;   
            }
        }

        // Packages where to look for the class
        String[] packages = {
            "gpframework",
            "gpframework.algorithms",
            "gpframework.algorithms.components",
            "gpframework.algorithms.components.mutations",
            "gpframework.algorithms.components.selections",
            "gpframework.algorithms.components.selections",
            "gpframework.indicators",
            "gpframework.indicators.sorting",
            "gpframework.indicators.order",
            "gpframework.indicators.majority",
            "gpframework.program",
            "gpframework.program.sorting",
            "gpframework.program.ordermajority",
        };

        // Instantiated object
        T t = null;
        
        // Scan packages
        for (String packageName : packages) 
        {
            try 
            {
                Constructor<T> constructor = (Constructor<T>) Class.forName(packageName +"."+ className).getConstructor(classes);
                t = constructor.newInstance(parameters);
                break;
            }  catch (Exception e) { /* Keep going */ } 
        }
        
        if (t == null)
            System.out.println("Object not created " + className + ", check your class name or look into fromClass() method for a solution.");
        
        return t;
    }
    
    /**
     * Generates a list of command line options for this program.
     * @param options reference to options.
     */
    private static void setupOptions(Options options) {
        
        Option opt;
        
        opt = new Option("t", "timeBudget", true, "maximum number of seconds to run each repetition of the experiment, e.g. 14400 seconds");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("e", "evaluationsBudget", true, "maximum number of evaluations for each experiment, e.g. 250.000");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("n", "inputSize", true, "problem input size, e.g. sorting n numbers");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("d", "debug", false, "enables debug messages");
        options.addOption(opt);
        
        opt = new Option("c", "cluster", false, "enables cluster mode");
        options.addOption(opt);
        
        // Problem options
        opt = new Option("ff", "FunctionFactory", true, "function factory for generating function nodes, e.g. JoinFactory");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("tf", "TerminalFactory", true, "terminal factory for generating terminal nodes, e.g. TerminalFactory");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("pf", "ProgramFactory", true, "program factory for generating programs, e.g. SortingProgramFactory");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("f", "FitnessFunction", true, "fitness function to use, e.g. HamSortednessFunction");
        opt.setRequired(true);
        options.addOption(opt);
        
        // Algorithm options
        opt = new Option("a", "Algorithm", true, "algorithm to use, e.g. Algorithm or SMOGP");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("s", "Selection", true, "selection criterion to generate new population from offsprings, e.g. StrictSelection, ParsimonySelection");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("mf", "MutationFactory", true, "factory to generate mutations, e.g. SingleMutationFactory, PoissonMutationFactory");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("bn", "batchName", true, "batch name to store on MongoDB");
        opt.setRequired(true);
        options.addOption(opt);
                
    }
}
