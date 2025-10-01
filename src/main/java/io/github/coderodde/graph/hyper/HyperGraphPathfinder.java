package io.github.coderodde.graph.hyper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * This class implements a point-to-point Dijkstra's algorithm over a hyper 
 * graph.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Oct 1, 2025)
 * @since 1.0.0 (Oct 1, 2025)
 */
public final class HyperGraphPathfinder {
    
    public static <I, W extends WeightFunction<W>>
         HyperGraphPath<I, W> find(HyperGraphNode<I, W> source,
                                   HyperGraphNode<I, W> target,
                                   WeightFunction<W> weights) {
        
        Queue<HeapNode<I, W>> open             = new PriorityQueue<>();
        Set<HyperGraphNode<I, W>> closed       = new HashSet<>();
        Map<HyperGraphNode<I, W>, W> distances = new HashMap<>();
        
        Map<HyperGraphNode<I, W>, HyperGraphNode<I, W>> parents = 
                new HashMap<>();
        
        List<HyperGraphEdge<I, W>> shortestPathHyperEdges = new ArrayList<>();
        
        open.add(new HeapNode(source, weights.zero()));
        
        while (!open.isEmpty()) {
            HyperGraphNode<I, W> current = open.remove().node;
            
            if (current.equals(target)) {
                List<HyperGraphNode<I, W>> shortestPathNodes = 
                        tracebackPath(current, parents);
                
                return new HyperGraphPath<>(shortestPathNodes,
                                            shortestPathHyperEdges);
            }
            
            closed.add(current);
            
            for (HyperGraphEdge<I, W> edge : current.getEdges()) {
                for (HyperGraphNode<I, W> child : edge.getVertices()) {
                    if (closed.contains(child)) {
                        continue;
                    }
                    
                    W tentative = 
                            edge.getWeight().apply(distances.get(current));
                    
                    if (!distances.containsKey(child) || 
                            weights.compare(distances.get(child), 
                                            tentative) > 1) {
                        
                        distances.put(child, tentative);
                        parents.put(child, current);
                        open.add(new HeapNode(child, tentative));
                        
                        shortestPathHyperEdges.addLast(edge);
                    }
                }
            }
        }
        
        throw new IllegalStateException("The hyper graph is disconnected");
    }
         
    private static <I, W extends WeightFunction<W>> 
        List<HyperGraphNode<I, W>> 
            tracebackPath(HyperGraphNode<I, W> target,
                          Map<HyperGraphNode<I, W>, 
                              HyperGraphNode<I, W>> parents) {
        
        List<HyperGraphNode<I, W>> path = new ArrayList<>();
        HyperGraphNode<I, W> current = target;
        
        while (current != null) {
            path.addLast(current);
            current = parents.get(current);
        }
        
        Collections.reverse(path);
        return path;
    }
         
    private static final class HeapNode<I, W extends WeightFunction<W>> {
        HyperGraphNode<I, W> node;
        W g;
        
        HeapNode(HyperGraphNode<I, W> node, W g) {
            this.node = node;
            this.g = g;
        }
    }
}
