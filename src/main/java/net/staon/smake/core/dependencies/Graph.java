/*
 * Copyright (C) 2022 Ondrej Starek (Staon)
 *
 * This file is part of smake2.
 *
 * SMake2 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * smake2 is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with smake2.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.staon.smake.core.dependencies;

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
public interface Graph<N> {
  /**
   * Public interface of graph's node
   */
  interface Node<N> {
    /**
     * Get data associated with the node
     */
    N getData();
    
    /**
     * Get out degree (number of out edges) of the node
     */
    int getOutDegree();
  }
  
  /**
   * Check whether a node is present in the graph
   */
  boolean containsNode(ID node_id_);
  
  /**
   * Add new node into the graph
   *
   * @param node_id_ ID of the node. The node must not be present in the graph
   *                 yet!
   * @param node_data_ Data associated with the new node.
   */
  void addNode(ID node_id_, N node_data_);
  
  /**
   * Add new dependency
   *
   * This method adds new dependency. If the dependency already exists
   * nothing happens (i.e. the dependency is not doubled).
   *
   * @param from_ ID of the source node
   * @param to_ ID of the target node
   * @return True if the dependency has been newly added. False if it
   *     has already existed.
   */
  boolean addDependency(ID from_, ID to_);
  
  /**
   * Get node
   *
   * @param node_id_ ID of the node. The node must exist!
   * @return The data
   */
  Graph.Node<N> getNode(ID node_id_);
  
  /**
   * Evaluate @a fn on every node in the graph
   *
   * @param fn The evaluation function
   */
  void forEachNode(BiConsumer<ID, GraphFull.Node<N>> fn);
  
  /**
   * Evaluate a function on every predecessor of a node
   *
   * @param node_id_ ID of the node
   * @param fn_ The function
   */
  void forEachPredecessor(ID node_id_, BiConsumer<ID, GraphFull.Node<N>> fn_);
}
