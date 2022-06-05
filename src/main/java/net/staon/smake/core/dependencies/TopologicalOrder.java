/*
 * Copyright (C) 2022 Ondrej Starek (Staon)
 *
 * This file is part of SMake2.
 *
 * SMake2 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OTest2 is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SMake2.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.staon.smake.core.dependencies;

import net.staon.smake.core.exception.DependencyCycleException;
import net.staon.smake.core.heap.BinomialHeap;
import net.staon.smake.core.heap.Heap;

import java.util.*;

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
  
  private static class Node {
    public ID id;
    public Color color;
    public int out_degree;
  }
  
  private final Map<ID, Node> nodes;
  private final Heap<Node> nodes_heap;
  private final Set<Node> grey_nodes;
  
  /**
   * Ctor
   *
   * @param graph_ The graph which the order is computed for.
   */
  public TopologicalOrder(Graph<N> graph_) {
    graph = graph_;
    nodes = new HashMap<>();
    nodes_heap = new BinomialHeap<>(Comparator.comparingInt(n -> n.out_degree));
    grey_nodes = new HashSet<>();
    
    /* -- fill the heap */
    graph.forEachNode(this::insertNewNode);
  }
  
  private void insertNewNode(ID id_, GraphFull.Node<N> node_) {
    var to_node_ = new Node();
    to_node_.id = id_;
    to_node_.color = Color.WHITE;
    to_node_.out_degree = node_.getOutDegree();
    nodes.put(id_, to_node_);
    nodes_heap.insert(to_node_);
  }
  
  public class OpenedLeaf {
    private Node node;
    private final N data;
    
    public OpenedLeaf(Node node_, N data_) {
      node = node_;
      data = data_;
    }
  
    /**
     * Close opened graph node
     *
     * This method makes the node black, and it decreases out degree of
     * all predecessors.
     */
    public void closeLeaf() {
      assert node != null;
      assert node.color == Color.GREY;
      
      node.color = Color.BLACK;
      grey_nodes.remove(node);

      /* -- decrease out-order of all predecessors */
      graph.forEachPredecessor(node.id, (id_, node_) -> {
        var pred = nodes.get(id_);
        assert pred != null && pred.color == Color.WHITE;
        
        var p_node_ = nodes.get(id_);
        assert p_node_ != null;
        --p_node_.out_degree;
        nodes_heap.update(p_node_);
      });
      
      node = null;
    }
    
    public N getData() {
      return data;
    }
  }
  
  /**
   * Get a leaf from the graph
   *
   * @return The cut leaf or null if no leaf is available.
   * @exception DependencyCycleException If dependency cycle is detected.
   */
  public OpenedLeaf cutLeaf() throws DependencyCycleException {
    if(nodes_heap.isEmpty())
      return null;
    
    var leaf = nodes_heap.getMin();
    assert leaf.out_degree >= 0;
    if(leaf.out_degree == 0) {
      /* -- the node at the top of the heap is a leaf */
      nodes_heap.pollMin();  // -- remove from the heap
      leaf.color = Color.GREY;
      grey_nodes.add(leaf);
      return new OpenedLeaf(leaf, graph.getNode(leaf.id).getData());
    }
    else {
      if(grey_nodes.isEmpty()) {
        /* -- There are no in-progress nodes but there is no leaf. A cycle
         *    is detected. */
        throw new DependencyCycleException();
      }
      return null;
    }
  }
  
  /**
   * Check that all nodes have been already cut
   */
  public boolean isEmpty() {
    return nodes_heap.isEmpty() && grey_nodes.isEmpty();
  }
  
  /**
   * Check whether a node exists in the graph
   */
  public boolean containsNode(ID node_id_) {
    return graph.containsNode(node_id_);
  }
  
  /**
   * Append new node into the underlying graph
   *
   * Warning! This method modifies the underlying graph. Just one topological
   * order object may exist if this method is used!
   *
   * @param node_id_ ID of the new node. It must be unique
   * @param data_ Data associated with the node
   */
  public void addNode(ID node_id_, N data_) {
    graph.addNode(node_id_, data_);
    var g_node_ = graph.getNode(node_id_);
    insertNewNode(node_id_, g_node_);
  }
  
  /**
   * Add new dependency into the graph
   *
   * This method adds a new dependency between two nodes. The source node
   * must be WHITE as dependency cannot be added to an already processed
   * or in-progress node.
   *
   * Warning! This method modifies the underlying graph. Just one topological
   * order object may exist if this method is used!
   *
   * @param from_ ID of the dependency source node
   * @param to_ ID of the dependency target node
   */
  public boolean addDependency(ID from_, ID to_) {
    var to_from_ = nodes.get(from_);
    assert to_from_ != null;
    assert to_from_.color == Color.WHITE;
    
    var to_to_ = nodes.get(to_);
    assert to_to_ != null;
    
    if(graph.addDependency(from_, to_)) {
      /* -- The dependency is new. Increase out degree of the source node
       *    only if the target source has not been processed yet - the degree
       *    is decreased during closing of the target node. */
      if(to_to_.color != Color.BLACK) {
        ++to_from_.out_degree;
        nodes_heap.update(to_from_);
      }
      
      return true;
    }
    else {
      return false;
    }
  }
}
