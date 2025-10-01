package io.github.coderodde.graph.hyper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @param <I> the type of the identity object.
 * @param <W> the type of the weights.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Sep 24, 2025)
 * @since 1.0.0 (Sep 24, 2025)
 */
public final class HyperGraphEdge<I, W extends WeightFunction<W>> {
    
    final Set<HyperGraphNode<I, W>> edgeNodes = new HashSet<>();
    private final W weight;
    
    public HyperGraphEdge(W weight) {
        this.weight = Objects.requireNonNull(weight);
    }
    
    @Override
    public int hashCode() {
        return edgeNodes.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        
        HyperGraphEdge<I, W> other = (HyperGraphEdge<I, W>) obj;
        return edgeNodes.equals(other.edgeNodes) && weight.equals(other.weight);
    }
    
    public W getWeight() {
        return weight;
    }
    
    public void connectNode(HyperGraphNode<I, W> node) {
        Objects.requireNonNull(node);
        edgeNodes.add(node);
        node.connectToEdge(this);
    }
    
    public boolean containsNode(HyperGraphNode<I, W> node) {
        return edgeNodes.contains(node);
    }
    
    public void deleteNode(HyperGraphNode<I, W> node) {
        node.disconnectFromEdge(this);
        edgeNodes.remove(node);
    }
    
    public void clear() {
        for (HyperGraphNode<I, W> node : edgeNodes) {
            node.disconnectFromEdge(this);
        }
        
        edgeNodes.clear();
    }
    
    public Set<HyperGraphNode<I, W>> getVertices() {
        return Collections.unmodifiableSet(edgeNodes);
    }
}
