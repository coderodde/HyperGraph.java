package io.github.coderodde.graph.hyper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class defines a hyperedge in a hypergraph. The type argument {@code J} 
 * is used for specifying the "identity" of a hyperedge. Two distinct hyperedges
 * must not share the same ID.
 * 
 * @param <I> the type of the node identity object.
 * @param <J> the type of the edge identity object.
 * @param <W> the type of the weights.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.1.0 (Oct 6, 2025)
 * @since 1.0.0 (Sep 24, 2025)
 */
public final class HyperGraphEdge<I, J, W> {
    
    private final J id;
    private final W weight;
    private final Set<HyperGraphNode<I, J, W>> edgeNodes = new HashSet<>();
    
    public HyperGraphEdge(J id, W weight) {
        this.id = Objects.requireNonNull(id);
        this.weight = Objects.requireNonNull(weight);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        boolean first = true;
        
        for (HyperGraphNode<I, J, W> node : edgeNodes) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            
            sb.append(node.toString());
        }
        
        sb.append(": weight = ");
        sb.append(weight);
        sb.append("}");
        return sb.toString();
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
        
        HyperGraphEdge<I, J, W> other = (HyperGraphEdge<I, J, W>) obj;
        return id.equals(other.id) && weight.equals(other.weight);
    }
    
    public J getId() {
        return id;
    }
    
    public W getWeight() {
        return weight;
    }
    
    public void connectNode(HyperGraphNode<I, J, W> node) {
        Objects.requireNonNull(node);
        edgeNodes.add(node);
        node.edges.add(this);
    }
    
    public boolean containsNode(HyperGraphNode<I, J, W> node) {
        return edgeNodes.contains(node);
    }
    
    public void disconnectNode(HyperGraphNode<I, J, W> node) {
        Objects.requireNonNull(node);
        edgeNodes.remove(node);
        node.edges.remove(this);
    }
    
    public void clear() {
        for (HyperGraphNode<I, J, W> node : edgeNodes) {
            node.edges.remove(this);
        }
        
        edgeNodes.clear();
    }
    
    public Set<HyperGraphNode<I, J, W>> getIncidentHyperNodes() {
        return Collections.unmodifiableSet(edgeNodes);
    }
}
