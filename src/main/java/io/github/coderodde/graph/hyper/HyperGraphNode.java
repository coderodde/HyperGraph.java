package io.github.coderodde.graph.hyper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @param <I> the type of the identity object.
 * @param <W> the type of the weights.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Sep 24, 2025)
 * @since 1.0.0 (Sep 24, 2025)
 */
public final class HyperGraphNode<I, J, W> {
   
    private final I id;
    protected final List<HyperGraphEdge<I, J, W>> edges = new ArrayList<>();
    
    public HyperGraphNode(I id) {
        this.id = Objects.requireNonNull(id);
    }
    
    public I getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return id.toString();
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
        
        HyperGraphNode<I, J, W> other = (HyperGraphNode<I, J, W>) obj;
        return id.equals(other.id);
    }
    
    public List<HyperGraphEdge<I, J, W>> getIncidentHyperEdges() {
        return Collections.unmodifiableList(edges);
    }
}
