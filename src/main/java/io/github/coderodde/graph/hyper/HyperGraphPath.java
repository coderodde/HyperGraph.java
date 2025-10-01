package io.github.coderodde.graph.hyper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class implements a hyper graph path, which is a list of nodes and a list
 * of hyper edges.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Oct 1, 2025)
 * @param <I>
 * @param <W>
 * @since 1.0.0 (Oct 1, 2025)
 */
public final class HyperGraphPath<I, W extends WeightFunction<W>> {
   
    private final List<HyperGraphNode<I, W>> nodes;
    private final List<HyperGraphEdge<I, W>> edges;
    
    public HyperGraphPath(List<HyperGraphNode<I, W>> nodes,
                          List<HyperGraphEdge<I, W>> edges) {
        
        Objects.requireNonNull(nodes);
        Objects.requireNonNull(edges);
        
        if (nodes.isEmpty()) {
            if (!edges.isEmpty()) {
                throw new IllegalArgumentException("Invalid hyper graph path");
            } else {
                this.nodes = nodes;
                this.edges = edges;
            }
        } else {
            if (nodes.size() - 1 != edges.size()) {
                throw new IllegalArgumentException(
                        "Node count, edge count mismatch");
            }
            
            check(nodes, edges);
            this.nodes = nodes;
            this.edges = edges;
        }
    }
    
    public boolean isNonExistent() {
        return nodes.isEmpty();
    }
    
    public Collection<HyperGraphNode<I, W>> getNodes() {
        return Collections.unmodifiableCollection(nodes);
    }
    
    public Collection<HyperGraphEdge<I, W>> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }
    
    private static <I, W extends WeightFunction<W>>
        void check(List<HyperGraphNode<I, W>> nodes,
                   List<HyperGraphEdge<I, W>> edges) {
            
        for (int i = 0; i < nodes.size() - 1; ++i) {
            HyperGraphNode<I, W> node1 = nodes.get(i);
            HyperGraphNode<I, W> node2 = nodes.get(i + 1);
            HyperGraphEdge<I, W> edge = edges.get(i);
            
            if (!edge.containsNode(node1)) {
                throw new IllegalArgumentException("Invalid hyper graph path");
            }
            
            if (!edge.containsNode(node2)) {
                throw new IllegalArgumentException("Invalid hyper graph path");
            }
        }
        
        if (!edges.get(edges.size() - 1)
                .containsNode(nodes.get(nodes.size() - 1))) {
            throw new IllegalArgumentException("Invalid hyper graph path");
        }
    }
}
