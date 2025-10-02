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
    
    public static <I, J, W>
         HyperGraphPath<I, J, W> find(HyperGraphNode<I, J, W> source,
                                      HyperGraphNode<I, J, W> target,
                                      WeightFunction<W> weights) {
        
        Queue<HeapNode<I, J, W>> open             = new PriorityQueue<>();
        Set<HyperGraphNode<I, J, W>> closed       = new HashSet<>();
        Map<HyperGraphNode<I, J, W>, W> distances = new HashMap<>();
        
        Map<HyperGraphNode<I, J, W>, HyperGraphNode<I, J, W>> parents = 
                new HashMap<>();
        
        open.add(new HeapNode(source, weights.zero(), weights));
        distances.put(source, weights.zero());
        parents.put(source, null);
        
        while (!open.isEmpty()) {
            HyperGraphNode<I, J, W> current = open.remove().node;
            
            if (current.equals(target)) {
                List<HyperGraphNode<I, J, W>> shortestPathNodes = 
                        tracebackPath(current, parents);
                
                List<HyperGraphEdge<I, J, W>> shortestPathHyperEdges = 
                        inferPathEdges(shortestPathNodes, weights);
                
                return new HyperGraphPath<>(shortestPathNodes,
                                            shortestPathHyperEdges,
                                            weights);
            }
            
            closed.add(current);
            
            for (HyperGraphEdge<I, J, W> edge 
                    : current.getIncidentHyperEdges()) {
                
                for (HyperGraphNode<I, J, W> child 
                        : edge.getIncidentHyperNodes()) {
                    
                    if (closed.contains(child)) {
                        continue;
                    }
                    
                    W tentative = 
                            weights.apply(edge.getWeight(),
                                          distances.get(current));
                    
                    if (!distances.containsKey(child) || 
                            weights.compare(distances.get(child), 
                                            tentative) > 1) {
                        
                        distances.put(child, tentative);
                        parents.put(child, current);
                        open.add(new HeapNode(child, 
                                              tentative, 
                                              weights));
                    }
                }
            }
        }
        
        throw new IllegalStateException("The hyper graph is disconnected");
    }
         
    private static <I, J, W> 
        List<HyperGraphNode<I, J, W>> 
            tracebackPath(HyperGraphNode<I, J, W> target,
                          Map<HyperGraphNode<I, J, W>, 
                              HyperGraphNode<I, J, W>> parents) {
        
        List<HyperGraphNode<I, J, W>> path = new ArrayList<>();
        HyperGraphNode<I, J, W> current = target;
        
        while (current != null) {
            path.addLast(current);
            current = parents.get(current);
        }
        
        Collections.reverse(path);
        return path;
    }
         
    private static <I, J, W> 
        List<HyperGraphEdge<I, J, W>> 
        inferPathEdges(List<HyperGraphNode<I, J, W>> shortestPathNodes, 
                       WeightFunction<W> weights) {
    
        List<HyperGraphEdge<I, J, W>> shortestPathEdges = 
                new ArrayList<>(shortestPathNodes.size() - 1);
        
        for (int i = 0; i < shortestPathNodes.size() - 1; ++i) {
            HyperGraphNode<I, J, W> node1 = shortestPathNodes.get(i);
            HyperGraphNode<I, J, W> node2 = shortestPathNodes.get(i + 1);
            
            HyperGraphEdge<I, J, W> edge = inferPathEdge(node1,
                                                         node2, 
                                                         weights);
            shortestPathEdges.add(edge);
        }
        
        return shortestPathEdges;
    }
        
    private static <I, J, W> HyperGraphEdge<I, J, W> 
        inferPathEdge(HyperGraphNode<I, J, W> node1,
                      HyperGraphNode<I, J, W> node2,
                      WeightFunction<W> weightFunction) {
        
        W smallestWeight = weightFunction.max();
        HyperGraphEdge<I, J, W> smallestHyperEdge = null;
        
        for (HyperGraphEdge<I, J, W> relay : node1.edges) {
            if (relay.edgeNodes.contains(node2)) {
                W currentWeight = relay.getWeight();
                
                if (weightFunction.compare(smallestWeight, currentWeight) > 0) {
                    smallestWeight = currentWeight;
                    smallestHyperEdge = relay;
                }
            }
        }
        
        if (smallestWeight == null) {
            throw new IllegalStateException("Should not get here");
        }
        
        return smallestHyperEdge; 
    }
            
    private static final class HeapNode<I, J, W> 
            implements Comparable<HeapNode<I, J, W>> {
        
        HyperGraphNode<I, J, W> node;
        W g;
        WeightFunction<W> weightFunction;
        
        HeapNode(HyperGraphNode<I, J, W> node, 
                 W g, 
                 WeightFunction<W> weightFunction) {
            this.node = node;
            this.g = g;
            this.weightFunction = weightFunction;
        }

        @Override
        public int compareTo(HeapNode<I, J, W> o) {
            return weightFunction.compare(g, o.g);
        }
    }
}
