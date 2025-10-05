package io.github.coderodde.graph.hyper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class implements a hypergraph path, which is a list of nodes and a list
 * of hyperedges.
 * 
 * @param <I> the identity object type.
 * @param <W> the weight type.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Oct 1, 2025)
 * @since 1.0.0 (Oct 1, 2025)
 */
public final class HyperGraphPath<I, J, W> {
   
    private final W weight;
    private final List<HyperGraphNode<I, J, W>> nodes;
    private final List<HyperGraphEdge<I, J, W>> edges;
    
    public HyperGraphPath(WeightFunction<W> weights) {
        this.weight = weights.zero();
        this.nodes = Collections.emptyList();
        this.edges = Collections.emptyList();
    }
    
    public HyperGraphPath(List<HyperGraphNode<I, J, W>> nodes,
                          List<HyperGraphEdge<I, J, W>> edges,
                          WeightFunction<W> weightFunction) {
        
        Objects.requireNonNull(nodes);
        Objects.requireNonNull(edges);
        
        if (nodes.isEmpty()) {
            if (!edges.isEmpty()) {
                throw new IllegalArgumentException("Invalid hyper graph path");
            } else {
                this.nodes = nodes;
                this.edges = edges;
                this.weight = weightFunction.zero();
            }
        } else {
            if (nodes.size() - 1 != edges.size()) {
                throw new IllegalArgumentException(
                        "Node count, edge count mismatch");
            }
            
            check(nodes, edges);
            this.nodes = nodes;
            this.edges = edges;
            
            W w = weightFunction.zero();
            
            for (HyperGraphNode<I, J, W> node : nodes) {
                w = weightFunction.apply(w, node.getWeight());
            }
            
            for (HyperGraphEdge<I, J, W> edge : edges) {
                w = weightFunction.apply(w, edge.getWeight());
            }
            
            this.weight = w;
        }
    }
    
    @Override
    public String toString() {
        if (isNonExistent()) {
            return "[: weight = 0]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        
        for (int i = 0; i < edges.size(); ++i) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            
            HyperGraphNode<I, J, W> node = nodes.get(i);
            HyperGraphEdge<I, J, W> edge = edges.get(i);
            
            sb.append(node.toString());
            sb.append(", ");
            sb.append(edge.toString());
        }
        
        sb.append(", ");
        sb.append(nodes.get(nodes.size() - 1).toString());
        sb.append(": total weight = ");
        sb.append(weight);
        sb.append("]");
        return sb.toString();
    }
    
    public W getWeight() {
        return weight;
    }
    
    public boolean isNonExistent() {
        return nodes.isEmpty();
    }
    
    public List<HyperGraphNode<I, J, W>> getPathHyperNodes() {
        return Collections.unmodifiableList(nodes);
    }
    
    public List<HyperGraphEdge<I, J, W>> getPathHyperEdges() {
        return Collections.unmodifiableList(edges);
    }
    
    private static <I, J, W> void check(List<HyperGraphNode<I, J, W>> nodes,
                                        List<HyperGraphEdge<I, J, W>> edges) {
            
        for (int i = 0; i < nodes.size() - 1; ++i) {
            HyperGraphNode<I, J, W> node1 = nodes.get(i);
            HyperGraphNode<I, J, W> node2 = nodes.get(i + 1);
            HyperGraphEdge<I, J, W> edge = edges.get(i);
            
            if (!edge.containsNode(node1)) {
                throw new IllegalArgumentException("Invalid hyper graph path");
            }
            
            if (!edge.containsNode(node2)) {
                throw new IllegalArgumentException("Invalid hyper graph path");
            }
        }
        
        if (!edges.get(edges.size() - 1)
                .containsNode(nodes.getLast())) {
            throw new IllegalArgumentException("Invalid hyper graph path");
        }
    }
}
