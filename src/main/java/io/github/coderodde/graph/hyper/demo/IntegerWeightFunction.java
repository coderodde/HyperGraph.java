package io.github.coderodde.graph.hyper.demo;

import io.github.coderodde.graph.hyper.WeightFunction;

/**
 * This class defines a weight function over integer valued weights.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Oct 1, 2025)
 * @since 1.0.0 (Oct 1, 2025)
 */
public final class IntegerWeightFunction implements WeightFunction<Integer> {

    @Override
    public Integer zero() {
        return 0;
    }

    @Override
    public Integer max() {
        return Integer.MAX_VALUE;
    }    

    @Override
    public Integer apply(Integer w1, Integer w2) {
        return w1 + w2;
    }

    @Override
    public int compare(Integer weight1, Integer weight2) {
        return Integer.compare(weight1,
                               weight2);
    }
}
