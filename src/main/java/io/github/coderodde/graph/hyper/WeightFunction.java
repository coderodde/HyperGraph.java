package io.github.coderodde.graph.hyper;

/**
 * This interface defines the API for the weight functions.
 * 
 * @param <W> the type of the weight.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Oct 2, 2025)
 * @since 1.0.0 (Oct 2, 2025)
 */
public interface WeightFunction<W> {
   
    W zero();
    W max();
    W apply(W w1, W w2);
    int compare(W weight1, W weight2);
}
