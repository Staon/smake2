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

import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Implementation of a subgraph
 *
 * This class keeps subgraph of graph. The subgraph is computed by
 * depth-first-search from roots specified in the constructor.
 *
 * @param <N> Type of data associated with graph's nodes
 */
public class GraphFiltered<N> implements Graph<N> {
  private final Graph<N> base_graph;
  private final Set<ID> nodes;
  
  /**
   * Ctor
   *
   * @param base_graph_ The base graph
   * @param roots_ Root nodes of the filtered graph (roots of the DFS filter)
   */
  public GraphFiltered(Graph<N> base_graph_, List<ID> roots_) {
    base_graph = base_graph_;
    nodes = new HashSet<>();
    
    runDFS(roots_);
  }
  
  private void runDFS(List<ID> roots_) {
    /* -- initialize the stack by roots */
    var stack_ = new ArrayDeque<ID>(Lists.reverse(roots_));
    
    /* -- DFS */
    while(!stack_.isEmpty()) {
      var top_ = stack_.pop();
      if(!nodes.contains(top_)) {
        nodes.add(top_);
        base_graph.forEachSuccessor(top_, (id_, node_) -> {
          stack_.push(id_);
        });
      }
    }
  }
  
  @Override
  public boolean containsNode(ID node_id_) {
    return nodes.contains(node_id_);
  }
  
  @Override
  public void addNode(ID node_id_, N node_data_) {
    base_graph.addNode(node_id_, node_data_);
    nodes.add(node_id_);
  }
  
  @Override
  public boolean addDependency(ID from_, ID to_) {
    assert from_ != to_;   /* -- loops are not allowed */
    assert nodes.contains(from_);
    assert nodes.contains(to_);
    
    return base_graph.addDependency(from_, to_);
  }
  
  @Override
  public Node<N> getNode(ID node_id_) {
    assert nodes.contains(node_id_);
    return base_graph.getNode(node_id_);
  }
  
  @Override
  public void forEachNode(BiConsumer<ID, Node<N>> fn_) {
    nodes.forEach((id_) -> {
      fn_.accept(id_, base_graph.getNode(id_));
    });
  }
  
  @Override
  public void forEachPredecessor(ID node_id_, BiConsumer<ID, Node<N>> fn_) {
    base_graph.forEachPredecessor(node_id_, (id_, node_) -> {
      if(nodes.contains(id_)) {
        fn_.accept(id_, node_);
      }
    });
  }
  
  @Override
  public void forEachSuccessor(ID node_id_, BiConsumer<ID, Node<N>> fn_) {
    base_graph.forEachSuccessor(node_id_, (id_, node_) -> {
      if(nodes.contains(id_)) {
        fn_.accept(id_, node_);
      }
    });
  }
}
