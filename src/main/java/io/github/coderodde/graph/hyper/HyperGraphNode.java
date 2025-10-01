package io.github.coderodde.graph.hyper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @param <I> the type of the identity object.
 * @param <W> the type of the weight function.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Sep 24, 2025)
 * @since 1.0.0 (Sep 24, 2025)
 */
public final class HyperGraphNode<I, W extends WeightFunction<W>> {
   
    private final I id;
    private final Set<HyperGraphEdge<I, W>> edges = new HashSet<>();
    
    public HyperGraphNode(I id) {
        this.id = Objects.requireNonNull(id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
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
        
        HyperGraphNode<I, W> other = (HyperGraphNode<I, W>) obj;
        return id.equals(other.id);
    }
    
    public void connectToEdge(HyperGraphEdge<I, W> edge) {
        edges.add(Objects.requireNonNull(edge));
    }
    
    public Set<HyperGraphEdge<I, W>> getEdges() {
        return Collections.unmodifiableSet(edges);
    }
    
    public void disconnectFromEdge(HyperGraphEdge<I, W> edge) {
        Objects.requireNonNull(edge);
        edge.edgeNodes.remove(this);
        edges.remove(edge);
    }
}
