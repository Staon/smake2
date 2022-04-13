package net.staon.smake.core.dependencies;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Generic dependency graph
 *
 * The dependency graph is simply an oriented graph. An oriented edge
 * means that "source node of the edge is dependent on the target node".
 * Inside smake code that usually means the target node must be processed
 * prior the source node.
 *
 * @param <N> Type of data associated with graph's nodes
 */
public class Graph<N> {
  /**
   * Public interface of graph's node
   */
  public interface Node<N> {
    /**
     * Get data associated with the node
     */
    N getData();
  
    /**
     * Get out degree (number of out edges) of the node
     */
    int getOutDegree();
  }
  
  private class NodeImpl implements Node<N> {
    private final ID id;
    private final N data;
    private final Set<NodeImpl> outs;
    private final Set<NodeImpl> ins;
    
    public NodeImpl(ID id_, N data_) {
      id = id_;
      data = data_;
      outs = new HashSet<>();
      ins = new HashSet<>();
    }
    
    public boolean addOutEdge(NodeImpl target_node_) {
      return outs.add(target_node_);
    }
    
    public void addInEdge(NodeImpl source_node_) {
      ins.add(source_node_);
    }
    
    public N getData() {
      return data;
    }
  
    public int getOutDegree() {
      return outs.size();
    }
  }
  
  HashMap<ID, NodeImpl> nodes;
  
  /**
   * Ctor - empty graph
   */
  public Graph() {
    nodes = new HashMap<>();
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
    
    var node = new NodeImpl(node_id_, node_data_);
    nodes.put(node_id_, node);
  }
  
  /**
   * Add dependency between two nodes
   *
   * The method adds new dependency. If the dependency already exists
   * nothing happens (i.e. the dependency is not doubled).
   *
   * @param from_ ID of the source node
   * @param to_ ID of the target source
   * @return true if the dependency has been newly added. False if the
   *     dependency has already existed.
   */
  public boolean addDependency(ID from_, ID to_) {
    assert from_ != to_;  /* -- loops are not allowed in the graph */
    
    var from_node_ = nodes.get(from_);
    assert from_node_ != null;
    var to_node_ = nodes.get(to_);
    assert to_node_ != null;
    
    /* -- create the graph edge */
    to_node_.addInEdge(from_node_);
    return from_node_.addOutEdge(to_node_);
  }
  
  /**
   * Get node
   *
   * @param node_id_ ID of the node. The node must exist!
   * @return The data
   */
  public Node<N> getNode(ID node_id_) {
    var node_ = nodes.get(node_id_);
    assert node_ != null;
    return node_;
  }
  
  /**
   * Evaluate @a fn on every node in the graph
   *
   * @param fn The evaluation function
   */
  public void forEachNode(BiConsumer<ID, Node<N>> fn) {
    nodes.forEach(fn);
  }
  
  /**
   * Evaluate a function on every predecessor of a node
   *
   * @param node_id_ ID of the node
   * @param fn_ The function
   */
  public void forEachPredecessor(ID node_id_, BiConsumer<ID, Node<N>> fn_) {
    var node_ = nodes.get(node_id_);
    assert node_ != null;
    node_.ins.forEach((n_) -> fn_.accept(n_.id, n_));
  }
}
