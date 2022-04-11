package net.staon.smake.core.dependencies;

import net.staon.smake.core.heap.BinomialHeap;
import net.staon.smake.core.heap.Heap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Computation of the graph topological order
 *
 * This class implements computation of topological order of a graph.
 * The algorithm is "cutting of leaves" - user in a cycle queries
 * leaves until the graph is empty or a cycle is detected.
 *
 * In addition, this class implements tricolor marking of nodes. This
 * feature allows adding dependencies into a graph which is already
 * in progress - new dependency must be added just between nodes
 * which have not been processed yet.
 *
 * @param <N> Data associated with graph nodes
 */
public class TopologicalOrder<N> {
  private final Graph<N> graph;
  
  private enum Color {
    WHITE,
    GREY,
    BLACK,
  }
  
  private class Node {
    public ID id;
    public Color color;
    public int out_degree;
  }
  
  private Map<ID, Node> nodes;
  private Heap<Node> nodes_heap;
  private Set<Node> grey_nodes;
  
  /**
   * Ctor
   *
   * @param graph_ The graph which the order is computed for.
   */
  public TopologicalOrder(Graph<N> graph_) {
    graph = graph_;
    nodes = new HashMap<>();
    nodes_heap = new BinomialHeap<>(Comparator.comparingInt(n -> n.out_degree));
    
    /* -- fill the heap */
    graph.forEachNode((id_, node_) -> {
      var to_node_ = new Node();
      to_node_.id = id_;
      to_node_.color = Color.WHITE;
      to_node_.out_degree = node_.getOutDegree();
      nodes.put(id_, to_node_);
      nodes_heap.insert(to_node_);
    });
  }
  
  public class OpenedLeaf {
    private TopologicalOrder<N> parent;
    private Node node;
    
    public OpenedLeaf(TopologicalOrder<N> parent_, Node node_) {
      parent = parent_;
      node = node_;
    }
  
    /**
     * Close opened graph node
     *
     * This method makes the node black, and it decreases out degree of
     * all predecessors.
     */
    public void closeLeaf() {
      assert parent != null;
      assert node.color == Color.GREY;
      
      node.color = Color.BLACK;
      parent.grey_nodes.remove(node);

      /* -- decrease out-order of all predecessors */
      parent.graph.forEachPredecessor(node.id, (id_, node_) -> {
        var pred = parent.nodes.get(id_);
        assert pred != null && pred.color == Color.WHITE;
        
        var p_node_ = parent.nodes.get(id_);
        assert p_node_ != null;
        --p_node_.out_degree;
        parent.nodes_heap.update(p_node_);
      });
      
      parent = null;
      node = null;
    }
  }
  
  /**
   * Get a leaf from the graph
   *
   * @return The cut leaf or null if no leaf is available.
   */
  public OpenedLeaf cutLeaf() {
    var leaf = nodes_heap.getMin();
    assert leaf.out_degree > 0;
    if(leaf.out_degree == 1) {
      /* -- the node at the top of the heap is a leaf */
      nodes_heap.pollMin();  // -- remove from the heap
      leaf.color = Color.GREY;
      grey_nodes.add(leaf);
      return new OpenedLeaf(this, leaf);
    }
    else {
      /* -- TODO: detect cycle (the grey list is empty) */
      return null;
    }
  }
}
