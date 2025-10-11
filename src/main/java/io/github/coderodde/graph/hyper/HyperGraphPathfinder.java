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
 * This class implements a point-to-point Dijkstra's algorithm over a 
 * hypergraph.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.1.0 (Oct 6, 2025)
 * @since 1.0.0 (Oct 1, 2025)
 */
public final class HyperGraphPathFinder {
    
    public static <I, J, W> 
        HyperGraphPath<I, J, W> biFind(HyperGraphNode<I, J, W> source,
                                       HyperGraphNode<I, J, W> target,
                                       WeightFunction<W> weights) {
        if (source.equals(target)) {
            return new HyperGraphPath<>(List.of(source),
                                        List.of(),
                                        weights);
        }
        
        Queue<HeapNode<I, J, W>> opena = new PriorityQueue<>();
        Queue<HeapNode<I, J, W>> openb = new PriorityQueue<>();
        Set<HyperGraphNode<I, J, W>> closeda = new HashSet<>();
        Set<HyperGraphNode<I, J, W>> closedb = new HashSet<>();
        Map<HyperGraphNode<I, J, W>, W> distancesa = new HashMap<>();
        Map<HyperGraphNode<I, J, W>, W> distancesb = new HashMap<>();
        Map<HyperGraphNode<I, J, W>, HyperGraphNode<I, J, W>> parentsa;
        Map<HyperGraphNode<I, J, W>, HyperGraphNode<I, J, W>> parentsb;
        
        W mu = weights.max();
        HyperGraphNode<I, J, W> toucha = null;
        HyperGraphNode<I, J, W> touchb = null;
        
        parentsa = new HashMap<>();
        parentsb = new HashMap<>();
        
        opena.add(new HeapNode<>(source,
                                 weights.zero(), 
                                 weights));
        
        openb.add(new HeapNode<>(target,
                                 weights.zero(),
                                 weights));
        
        distancesa.put(source, weights.zero());
        distancesb.put(target, weights.zero());
        
        parentsa.put(source, null);
        parentsb.put(target, null);
        
        while (!opena.isEmpty() && !openb.isEmpty()) {
            
            HyperGraphNode<I, J, W> currenta = opena.peek().node;
            HyperGraphNode<I, J, W> currentb = openb.peek().node;
            
            if (opena.size() + closeda.size() <= 
                openb.size() + closedb.size()) {
                
                opena.remove();
                closeda.add(currenta);

                for (HyperGraphEdge<I, J, W> edge 
                        : currenta.getIncidentHyperEdges()) {

                    for (HyperGraphNode<I, J, W> child 
                            : edge.getIncidentHyperNodes()) {

                        if (closeda.contains(child)) {
                            continue;
                        }

                        W tentative = weights.apply(distancesa.get(currenta), 
                                                    edge.getWeight());

                        tentative = weights.apply(tentative, child.getWeight());

                        if (!distancesa.containsKey(child) 
                                || weights.compare(distancesa.get(child), 
                                                   tentative) > 0) {

                            distancesa.put(child, tentative);
                            parentsa.put(child, currenta);
                            opena.add(new HeapNode<>(child,
                                                     tentative, 
                                                     weights));
                            
                            if (closedb.contains(child)) {
                                W w = weights.apply(distancesa.get(currenta),
                                                    edge.getWeight());

                                w = weights.apply(w, child.getWeight());
                                w = weights.apply(w, distancesb.get(child));

                                if (weights.compare(mu, w) > 0) {
                                    mu = w;
                                    toucha = currenta;
                                    touchb = child;
                                }
                            }
                        }
                    }
                }
            } else {
                openb.remove();
                closedb.add(currentb);
                
                for (HyperGraphEdge<I, J, W> edge 
                    : currentb.getIncidentHyperEdges()) {
                
                    for (HyperGraphNode<I, J, W> parent 
                            : edge.getIncidentHyperNodes()) {

                        if (closedb.contains(parent)) {
                            continue;
                        }

                        W tentative = weights.apply(distancesb.get(currentb), 
                                                    edge.getWeight());

                        tentative = weights.apply(tentative, 
                                                  parent.getWeight());

                        if (!distancesb.containsKey(parent) 
                                || weights.compare(distancesb.get(parent), 
                                                   tentative) > 0) {

                            distancesb.put(parent, tentative);
                            parentsb.put(parent, currentb);
                            openb.add(new HeapNode<>(parent,
                                                     tentative, 
                                                     weights));
                            
                            if (closeda.contains(parent)) {
                                W w = weights.apply(distancesb.get(currentb),
                                                    edge.getWeight());

                                w = weights.apply(w, parent.getWeight());
                                w = weights.apply(w, distancesa.get(parent));

                                if (weights.compare(mu, w) > 0) {
                                    mu = w;
                                    toucha = parent;
                                    touchb = currentb;
                                }
                            }
                        }
                    }
                }
            }
            
            if (distancesa.containsKey(currenta) &&
                distancesb.containsKey(currentb)) {

                W score = weights.apply(distancesa.get(currenta),
                                        distancesb.get(currentb));

                if (weights.compare(score, mu) > 0) {

                    List<HyperGraphNode<I, J, W>> pathNodes = 
                            tracebackPath(toucha,
                                          touchb,
                                          parentsa,
                                          parentsb);

                    List<HyperGraphEdge<I, J, W>> pathEdges = 
                            inferPathEdges(pathNodes, 
                                           weights);

                    return new HyperGraphPath<>(pathNodes,
                                                pathEdges,
                                                weights);
                }
            }
        }
        
        // Return empty path:
        return new HyperGraphPath<>(weights);
    }
    
    public static <I, J, W>
         HyperGraphPath<I, J, W> find(HyperGraphNode<I, J, W> source,
                                      HyperGraphNode<I, J, W> target,
                                      WeightFunction<W> weights) {
        
        Queue<HeapNode<I, J, W>> open             = new PriorityQueue<>();
        Set<HyperGraphNode<I, J, W>> closed       = new HashSet<>();
        Map<HyperGraphNode<I, J, W>, W> distances = new HashMap<>();
        
        Map<HyperGraphNode<I, J, W>, HyperGraphNode<I, J, W>> parents = 
                new HashMap<>();
        
        open.add(new HeapNode(source, 
                              weights.zero(),
                              weights));
        
        distances.put(source, 
                      weights.zero());
        
        parents.put(source, 
                    null);
        
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
                    
                    // Add edge weight:
                    W tentative = 
                            weights.apply(distances.get(current),
                                          edge.getWeight());
                    
                    // Add child weight:
                    tentative = weights.apply(tentative, child.getWeight());
                    
                    if (!distances.containsKey(child) || 
                            weights.compare(distances.get(child), 
                                            tentative) > 0) {
                        
                        distances.put(child, tentative);
                        parents.put(child, current);
                        open.add(new HeapNode(child, 
                                              tentative, 
                                              weights));
                    }
                }
            }
        }
        
        // Return empty path:
        return new HyperGraphPath<>(weights);
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
        List<HyperGraphNode<I, J, W>>
            tracebackPath(HyperGraphNode<I, J, W> toucha,
                          HyperGraphNode<I, J, W> touchb,
                          Map<HyperGraphNode<I, J, W>,
                              HyperGraphNode<I, J, W>> parentsa,
                          Map<HyperGraphNode<I, J, W>,
                              HyperGraphNode<I, J, W>> parentsb) {
                
        List<HyperGraphNode<I, J, W>> prefixPath = tracebackPath(toucha,
                                                                 parentsa);
        
        List<HyperGraphNode<I, J, W>> suffixPath = new ArrayList<>();
        
        HyperGraphNode<I, J, W> current = touchb;
        
        while (current != null) {
            suffixPath.add(current);
            current = parentsb.get(current);
        }
        
        prefixPath.addAll(suffixPath);
        return prefixPath;
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
            if (relay.getIncidentHyperNodes().contains(node2)) {
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
