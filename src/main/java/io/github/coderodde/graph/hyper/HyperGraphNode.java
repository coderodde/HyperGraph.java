package io.github.coderodde.graph.hyper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class defines a node in a hypergraph. The type argument {@code I} is the
 * identity type of a hyperedge. Two distinct hypernodes must not share the same
 * ID.
 * 
 * @param <I> the type of the hypernode identity object.
 * @param <J> the type of the hyperedge identity object.
 * @param <W> the type of the hypernode weight.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.1.0 (Oct 6, 2025)
 * @since 1.0.0 (Sep 24, 2025)
 */
public final class HyperGraphNode<I, J, W> {
   
    private final I id;
    private final W weight;
    protected final Set<HyperGraphEdge<I, J, W>> edges = new HashSet<>();
    
    public HyperGraphNode(I id, W weight) {
        this.id = Objects.requireNonNull(id);
        this.weight = Objects.requireNonNull(weight);
    }
    
    public I getId() {
        return id;
    }
    
    public W getWeight() {
        return weight;
    }
    
    @Override
    public String toString() {
        return String.format("[%s: weight = %s]",
                             id.toString(), 
                             weight.toString());
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
    
    public Set<HyperGraphEdge<I, J, W>> getIncidentHyperEdges() {
        return Collections.unmodifiableSet(edges);
    }
}
