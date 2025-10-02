package io.github.coderodde.graph.hyper;

/**
 *
 * @param <W> the type of the weight.
 * 
 * @author rodio
 */
public interface WeightFunction<W> {
   
    W zero();
    W max();
    W apply(W w1, W w2);
    int compare(W weight1, W weight2);
}
