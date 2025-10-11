package io.github.coderodde.graph.hyper;

import io.github.coderodde.graph.hyper.demo.IntegerWeightFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class HyperGraphPathfinderTest {
    
    private final IntegerWeightFunction weightFunction =
            new IntegerWeightFunction();
    
    @Test
    public void find() {
        HyperGraphNode<Integer, Integer, Integer> node1 = new HyperGraphNode<>(1, 1);
        HyperGraphNode<Integer, Integer, Integer> node2 = new HyperGraphNode<>(2, 2);
        HyperGraphNode<Integer, Integer, Integer> node3 = new HyperGraphNode<>(3, 3);
        
        HyperGraphEdge<Integer, Integer, Integer> edge1 = new HyperGraphEdge<>(1, 3);
        HyperGraphEdge<Integer, Integer, Integer> edge2 = new HyperGraphEdge<>(2, 4);
        
        edge1.connectNode(node1);
        edge1.connectNode(node2);
        edge2.connectNode(node2);
        edge2.connectNode(node3);
        
        HyperGraphPath<Integer, Integer, Integer> path = 
                HyperGraphPathFinder.find(node1,
                                          node3, 
                                          weightFunction);
        
        assertEquals(3, path.getPathHyperNodes().size());
        assertEquals(2, path.getPathHyperEdges().size());
        assertEquals((Integer) 13, path.getWeight());
        
        assertEquals(node1, path.getPathHyperNodes().get(0));
        assertEquals(node2, path.getPathHyperNodes().get(1));
        assertEquals(node3, path.getPathHyperNodes().get(2));
        
        assertEquals(edge1, path.getPathHyperEdges().get(0));
        assertEquals(edge2, path.getPathHyperEdges().get(1));
    }
    
    @Test
    public void biFind() {
        HyperGraphNode<Integer, Integer, Integer> node1 = new HyperGraphNode<>(1, 1);
        HyperGraphNode<Integer, Integer, Integer> node2 = new HyperGraphNode<>(2, 2);
        HyperGraphNode<Integer, Integer, Integer> node3 = new HyperGraphNode<>(3, 3);
        
        HyperGraphEdge<Integer, Integer, Integer> edge1 = new HyperGraphEdge<>(1, 3);
        HyperGraphEdge<Integer, Integer, Integer> edge2 = new HyperGraphEdge<>(2, 4);
        
        edge1.connectNode(node1);
        edge1.connectNode(node2);
        edge2.connectNode(node2);
        edge2.connectNode(node3);
        
        HyperGraphPath<Integer, Integer, Integer> path = 
                HyperGraphPathFinder.biFind(node1,
                                            node3, 
                                            weightFunction);
        
        assertEquals(3, path.getPathHyperNodes().size());
        assertEquals(2, path.getPathHyperEdges().size());
        assertEquals((Integer) 13, path.getWeight());
        
        assertEquals(node1, path.getPathHyperNodes().get(0));
        assertEquals(node2, path.getPathHyperNodes().get(1));
        assertEquals(node3, path.getPathHyperNodes().get(2));
        
        assertEquals(edge1, path.getPathHyperEdges().get(0));
        assertEquals(edge2, path.getPathHyperEdges().get(1));
    }
    
    @Test
    public void throwOnDisconnectedGraph() {
        HyperGraphNode<Integer, Integer, Integer> node1 = new HyperGraphNode<>(1, 10);
        HyperGraphNode<Integer, Integer, Integer> node2 = new HyperGraphNode<>(2, 20);
        
        HyperGraphPath<Integer, Integer, Integer> path = 
                HyperGraphPathFinder.find(node1, 
                                          node2, 
                                          weightFunction);
        
        assertEquals(Integer.valueOf(0), path.getWeight());
        assertTrue(path.getPathHyperNodes().isEmpty());
        assertTrue(path.getPathHyperEdges().isEmpty());
    }
}
