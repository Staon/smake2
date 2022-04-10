package net.staon.smake.core.dependencies;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

import java.util.*;

/**
 * Generic dependency graph
 * <p>
 * The dependency graph contains nodes and oriented edges between them. The structure supports the algorithm
 * of "cutting leaves" - it's possible to ask for leaves and remove them from graph.
 * <p>
 * The graph implements tricolor semantics - it's possible to add new dependencies and new nodes while
 * the cutting leaves is in progress. White nodes are untouched (still out of progress), grey nodes
 * are currently in progress and black nodes are finished. New dependency can be added just from a white
 * node to any other node. Adding dependency from grey of black will cause an exception.
 *
 * @param <N> Type of data associated with graph nodes
 */
public class Graph<N> {
  /* -- tri-color marking of the graph nodes */
  private enum Color {
    WHITE,
    GREY,
    BLACK,
  }
  
  private class Node {
    Color color;
    private final N data;
    Set<Node> outs;
    Set<Node> ins;
    
    public Node(N data_) {
      color = Color.WHITE;
      data = data_;
    }
    
    public N getData() {
      return data;
    }
    
    public N openNode() {
      assert color == Color.WHITE;
      
      color = Color.GREY;
      return data;
    }
    
    public N closeNode() {
      assert color == Color.GREY;
      
      color = Color.BLACK;
      return data;
    }
    
    public void addOutEdge(Node target_node_) {
      outs.add(target_node_);
    }
    
    public void addInEdge(Node source_node_) {
      ins.add(source_node_);
    }
    
    public int outDepSize() {
      return outs.size();
    }
  }
  
  HashMap<ID, Node> nodes;
  SortedMultiset<Node> nodes_out;
  
  /**
   * Ctor - empty graph
   */
  public Graph() {
    nodes = new HashMap<>();
    nodes_out = TreeMultiset.create(Comparator.comparingInt(Node::outDepSize));
  }
  
  /**
   * Add new node into the graph
   *
   * @param node_id_   Identifier of the new node. The node must not exist yet!
   * @param node_data_ Generic data stored with the node
   */
  public void addNode(ID node_id_, N node_data_) {
    assert node_id_ != null;
    assert !nodes.containsKey(node_id_);
    
    var node = new Node(node_data_);
    nodes.put(node_id_, node);
    nodes_out.add(node);
  }
  
  public void addDependency(ID from_, ID to_) {
    var from_node_ = nodes.get(from_);
    assert from_node_ != null;
    var to_node_ = nodes.get(to_);
    assert to_node_ != null;
    
    /* -- create the graph edge */
    from_node_.addOutEdge(to_node_);
    to_node_.addInEdge(from_node_);
    
    /* -- recalculate position of the target node in the queue */
  }
  
  /**
   * Open next graph leaf
   * <p>
   * The method removes first leaf from the priority queue and returns it. If there is no leaf in the queue
   * the null is returned.
   *
   * @return Data attached to the leaf node. Null if there is no leaf to be opened.
   */
  public N openLeaf() {
    var top_element_ = nodes_out.firstEntry();
    if(top_element_ == null)  /* -- empty queue */
      return null;
    
    /* -- check whether the node is a leaf */
    var top_ = top_element_.getElement();
    if(top_.outDepSize() > 0)
      return null;
    
    /* -- remove from the queue and return stored node data */
    var unused = nodes_out.pollFirstEntry();
    return top_.openNode();
  }
  
  /**
   * Close currently opened leaf
   *
   * @param node_id_ ID of the leaf. The leaf must be already opened by the openLeaf() method!
   * @return Node data associated with the leaf.
   */
  public N closeLeaf(ID node_id_) {
    assert nodes.containsKey(node_id_);
    
    var node_ = nodes.get(node_id_);
    return node_.closeNode();
  }
  
  /**
   * Check whether the queue of nodes to be processed is empty
   */
  public boolean isQueueEmpty() {
    return nodes_out.isEmpty();
  }
}
