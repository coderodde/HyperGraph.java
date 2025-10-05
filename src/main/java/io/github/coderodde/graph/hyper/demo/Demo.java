package io.github.coderodde.graph.hyper.demo;

import io.github.coderodde.graph.hyper.HyperGraphEdge;
import io.github.coderodde.graph.hyper.HyperGraphNode;
import io.github.coderodde.graph.hyper.HyperGraphPath;
import io.github.coderodde.graph.hyper.HyperGraphPathfinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Oct 1, 2025)
 * @since 1.0.0 (Oct 1, 2025)
 */
public final class Demo {
    
    private static final int HYPER_NODES = 1_000_000;
    private static final int HYPER_EDGES = 400_000;
    private static final int MINIMUM_HYPER_EDGE_SIZE = 2;
    private static final int MAXIMUM_HYPER_EDGE_SIZE = 8;
    private static final int MINIMUM_HYPER_EDGE_WEIGHT = 1;
    private static final int MAXIMUM_HYPER_EDGE_WEIGHT = 10;
    private static final int MINIMUM_HYPER_NODE_WEIGHT = 0;
    private static final int MAXIMUM_HYPER_NODE_WEIGHT = 5;
    
    private static final Random RND = new Random(13L);
    
    public static void main(String[] args) {
        long ta = System.currentTimeMillis();
        
        List<HyperGraphNode<Integer, Integer, Integer>> hyperGraph = 
                getRandomHyperGraph();

        long tb = System.currentTimeMillis();
        
        System.out.println(
                "Constructed the demo hyper graph in " 
                        + (tb - ta)
                        + " milliseconds.");
        
        HyperGraphNode<Integer, Integer, Integer> source = choose(hyperGraph);
        HyperGraphNode<Integer, Integer, Integer> target = choose(hyperGraph);
        
        ta = System.currentTimeMillis();
        
        HyperGraphPath<Integer, Integer, Integer> path =
                HyperGraphPathfinder.find(source, 
                                          target,
                                          new IntegerWeightFunction());
        tb = System.currentTimeMillis();
        
        printPath(path);
        
        System.out.println("Duration: " + (tb - ta) + " milliseconds.");
    }
    
    private static void printPath(HyperGraphPath<Integer, 
                                                 Integer, 
                                                 Integer> path) {
        for (int i = 0; i < path.getPathHyperEdges().size(); ++i) {
            System.out.println(path.getPathHyperNodes().get(i));
            System.out.println(path.getPathHyperEdges().get(i));
        }
        
        System.out.println(
                path.getPathHyperNodes()
                    .getLast()
                    .toString());
        
        System.out.println("Path weight = " + path.getWeight());
    }
    
    private static List<HyperGraphNode<Integer, 
                                       Integer, 
                                       Integer>> getRandomHyperGraph() {
                                           
        List<HyperGraphNode<Integer, Integer, Integer>> nodes =
                new ArrayList<>();
        
        createHyperNodes(nodes);
        createHyperEdges(nodes);
        
        return nodes;
    }
    
    private static 
        void createHyperNodes(List<HyperGraphNode<Integer, 
                                                  Integer, 
                                                  Integer>> nodes) {
            
        for (int id = 0; id < HYPER_NODES; ++id) {
            nodes.add(new HyperGraphNode<>(id, getRandomNodeWeight()));
        }
    }
    
    private static 
        void createHyperEdges(List<HyperGraphNode<Integer,
                                                  Integer, 
                                                  Integer>> nodes) {
            
        for (int i = 0; i < HYPER_EDGES; ++i) {
            Integer weight = getRandomEdgeWeight();
            
            HyperGraphEdge<Integer, Integer, Integer> edge =
                    new HyperGraphEdge<>(i, weight);
            
            int hyperEdgeSize = getHyperEdgeSize();
            
            for (int j = 0; j < hyperEdgeSize; ++j) {
                HyperGraphNode<Integer, Integer, Integer> node = choose(nodes);
                edge.connectNode(node);
            }
        }
    }
        
    private static int getRandomEdgeWeight() {
        return MINIMUM_HYPER_EDGE_WEIGHT + 
               RND.nextInt(MAXIMUM_HYPER_EDGE_WEIGHT + 1);
    }
        
    private static int getRandomNodeWeight() {
        return MINIMUM_HYPER_NODE_WEIGHT + 
               RND.nextInt(MAXIMUM_HYPER_NODE_WEIGHT + 1);
    }
        
    private static int getHyperEdgeSize() {
        return MINIMUM_HYPER_EDGE_SIZE + RND.nextInt(MAXIMUM_HYPER_EDGE_SIZE -
                                                     MINIMUM_HYPER_EDGE_SIZE +
                                                     1);
    }
    
    private static HyperGraphNode<Integer, Integer, Integer> 
        choose(List<HyperGraphNode<Integer, Integer, Integer>> nodes) {
            
        return nodes.get(RND.nextInt(nodes.size()));
    }
}
