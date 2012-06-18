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

package gpframework.algorithms.components;

import gpframework.algorithms.components.mutations.Deletion;
import gpframework.algorithms.components.mutations.Insertion;
import gpframework.algorithms.components.mutations.Replacement;
import gpframework.common.Utils;
import gpframework.problems.Problem;
import java.util.ArrayList;
import java.util.List;

/**
 * Mutation factory which generates a list one single Mutation (with equal
 * probability of selecting a Deletion, an Insertion or a Replacement).
 */
public class SingleMutationFactory extends MutationFactory {

    @Override
    public List<Mutation> generate(Problem problem) {
        List<Mutation> chosen = new ArrayList<Mutation>();
        
        int pick = Utils.random.nextInt(3);
        
        switch(pick) 
        {
            case 0: 
                chosen.add(new Insertion(problem));
                break;
            case 1:
                chosen.add(new Deletion(problem));
                break;
            case 2:
                chosen.add(new Replacement(problem));
                break;
            default:
                chosen = null;
                break;
        }
                
        return chosen;
    }
}
