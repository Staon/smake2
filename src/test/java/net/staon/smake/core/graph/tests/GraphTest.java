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
package net.staon.smake.core.graph.tests;

import net.staon.smake.core.dependencies.*;
import net.staon.smake.core.exception.DependencyCycleException;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static com.google.common.truth.Truth.assertThat;

public class GraphTest {
  @Test
  public void topologicalOrderSimple() throws DependencyCycleException {
    Graph<String> graph_ = new GraphFull<>();
    
    var id1_ = new ID("1");
    var id2_ = new ID("2");
    var id3_ = new ID("3");
    var id4_ = new ID("4");
    var id5_ = new ID("5");
    var id6_ = new ID("6");
    var id7_ = new ID("7");
    
    graph_.addNode(id1_, "Node 1");
    graph_.addNode(id2_, "Node 2");
    graph_.addNode(id3_, "Node 3");
    graph_.addNode(id4_, "Node 4");
    graph_.addNode(id5_, "Node 5");
    graph_.addNode(id6_, "Node 6");
    graph_.addNode(id7_, "Node 7");
    
    graph_.addDependency(id2_, id1_);

    graph_.addDependency(id3_, id1_);
    graph_.addDependency(id3_, id2_);
  
    graph_.addDependency(id4_, id1_);
    graph_.addDependency(id4_, id2_);
    graph_.addDependency(id4_, id3_);
  
    graph_.addDependency(id5_, id1_);
    graph_.addDependency(id5_, id2_);
    graph_.addDependency(id5_, id3_);
    graph_.addDependency(id5_, id4_);
  
    graph_.addDependency(id6_, id1_);
    graph_.addDependency(id6_, id2_);
    graph_.addDependency(id6_, id3_);
    graph_.addDependency(id6_, id4_);
    graph_.addDependency(id6_, id5_);
  
    graph_.addDependency(id7_, id1_);
    graph_.addDependency(id7_, id2_);
    graph_.addDependency(id7_, id3_);
    graph_.addDependency(id7_, id4_);
    graph_.addDependency(id7_, id5_);
    graph_.addDependency(id7_, id6_);
  
    var to_ = new TopologicalOrder<>(graph_);
    assertFalse(to_.isEmpty());
    
    var leaf_ = to_.cutLeaf();
    assertEquals("Node 1", leaf_.getData());
    var empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
    
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertEquals("Node 2", leaf_.getData());
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
  
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertEquals("Node 3", leaf_.getData());
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
  
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertEquals("Node 4", leaf_.getData());
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
  
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertEquals("Node 5", leaf_.getData());
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
  
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertEquals("Node 6", leaf_.getData());
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
  
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertEquals("Node 7", leaf_.getData());
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
    
    leaf_.closeLeaf();
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertTrue(to_.isEmpty());
  }
  
  @Test
  public void topologicalOrderSimple2() throws DependencyCycleException {
    Graph<String> graph_ = new GraphFull<>();
    
    var id1_ = new ID("1");
    var id2_ = new ID("2");
    var id3_ = new ID("3");
    var id4_ = new ID("4");
    var id5_ = new ID("5");
    var id6_ = new ID("6");
    var id7_ = new ID("7");
    
    graph_.addNode(id1_, "Node 1");
    graph_.addNode(id2_, "Node 2");
    graph_.addNode(id3_, "Node 3");
    graph_.addNode(id4_, "Node 4");
    graph_.addNode(id5_, "Node 5");
    graph_.addNode(id6_, "Node 6");
    graph_.addNode(id7_, "Node 7");
    
    graph_.addDependency(id3_, id1_);
    graph_.addDependency(id3_, id2_);
    
    graph_.addDependency(id4_, id3_);
    graph_.addDependency(id5_, id3_);
    graph_.addDependency(id6_, id3_);
    
    graph_.addDependency(id7_, id4_);
    graph_.addDependency(id7_, id5_);
    graph_.addDependency(id7_, id6_);
    
    var to_ = new TopologicalOrder<>(graph_);
    assertFalse(to_.isEmpty());

    var leaf1_ = to_.cutLeaf();
    var leaf2_ = to_.cutLeaf();
    assertFalse(to_.isEmpty());
    assertTrue(
      Objects.equals(leaf1_.getData(), "Node 1") ||
        Objects.equals(leaf1_.getData(), "Node 2"));
    assertTrue(
      Objects.equals(leaf2_.getData(), "Node 1") ||
        Objects.equals(leaf2_.getData(), "Node 2"));
    assertNotSame(leaf1_.getData(), leaf2_.getData());
  
    var empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
    
    leaf1_.closeLeaf();
    
    /* -- leaf2_ is still opened -> the Node 3 is not a leaf yet */
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
    
    leaf2_.closeLeaf();
    
    /* -- the Node 3 is the only one leaf now */
    leaf1_ = to_.cutLeaf();
    assertFalse(to_.isEmpty());
    assertEquals("Node 3", leaf1_.getData());
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    
    leaf1_.closeLeaf();
    
    /* -- Nodes 4, 5 and 6 are leaves now */
    leaf1_ = to_.cutLeaf();
    leaf2_ = to_.cutLeaf();
    var leaf3_ = to_.cutLeaf();
    assertFalse(to_.isEmpty());
    assertTrue(
      Objects.equals(leaf1_.getData(), "Node 4") ||
        Objects.equals(leaf1_.getData(), "Node 5") ||
        Objects.equals(leaf1_.getData(), "Node 6"));
    assertTrue(
      Objects.equals(leaf2_.getData(), "Node 4") ||
        Objects.equals(leaf2_.getData(), "Node 5") ||
        Objects.equals(leaf2_.getData(), "Node 6"));
    assertTrue(
      Objects.equals(leaf3_.getData(), "Node 4") ||
        Objects.equals(leaf3_.getData(), "Node 5") ||
        Objects.equals(leaf3_.getData(), "Node 6"));
    assertNotSame(leaf1_.getData(), leaf2_.getData());
    assertNotSame(leaf1_.getData(), leaf3_.getData());
    assertNotSame(leaf2_.getData(), leaf3_.getData());
  
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
  
    leaf3_.closeLeaf();
  
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
    
    leaf2_.closeLeaf();
  
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
    
    leaf1_.closeLeaf();
  
    /* -- the Node 7 is the last remaining in the graph */
    leaf1_ = to_.cutLeaf();
    assertFalse(to_.isEmpty());
    assertEquals("Node 7", leaf1_.getData());
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    
    leaf1_.closeLeaf();

    /* -- empty graph */
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertTrue(to_.isEmpty());
  }
  
  @Test
  public void topologicalOrderCycle() throws DependencyCycleException {
    Graph<String> graph_ = new GraphFull<>();
  
    var id1_ = new ID("1");
    var id2_ = new ID("2");
    var id3_ = new ID("3");
    var id4_ = new ID("4");
    var id5_ = new ID("5");
    var id6_ = new ID("6");
    var id7_ = new ID("7");
  
    graph_.addNode(id1_, "Node 1");
    graph_.addNode(id2_, "Node 2");
    graph_.addNode(id3_, "Node 3");
    graph_.addNode(id4_, "Node 4");
    graph_.addNode(id5_, "Node 5");
    graph_.addNode(id6_, "Node 6");
    graph_.addNode(id7_, "Node 7");
  
    graph_.addDependency(id2_, id1_);
    graph_.addDependency(id3_, id2_);
    graph_.addDependency(id4_, id3_);
    graph_.addDependency(id5_, id4_);
    graph_.addDependency(id2_, id5_);
    graph_.addDependency(id6_, id5_);
    graph_.addDependency(id7_, id6_);
    
    var to_ = new TopologicalOrder<>(graph_);
    
    var leaf_ = to_.cutLeaf();
    assertFalse(to_.isEmpty());
    assertEquals("Node 1", leaf_.getData());
    
    var empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
    
    leaf_.closeLeaf();
    
    /* -- dependency cycle is detected now */
    assertThrowsExactly(DependencyCycleException.class, to_::cutLeaf);
    assertFalse(to_.isEmpty());
  }
  
  @Test
  public void topologicalOrderDynamic() throws DependencyCycleException {
    Graph<String> graph_ = new GraphFull<>();
  
    var id1_ = new ID("1");
    var id2_ = new ID("2");
    var id3_ = new ID("3");
    var id4_ = new ID("4");
    var id5_ = new ID("5");
    var id6_ = new ID("6");
  
    graph_.addNode(id1_, "Node 1");
    graph_.addNode(id2_, "Node 2");
    graph_.addNode(id3_, "Node 3");
    graph_.addNode(id4_, "Node 4");
    graph_.addNode(id5_, "Node 5");
  
    graph_.addDependency(id2_, id1_);
    graph_.addDependency(id3_, id1_);
    graph_.addDependency(id4_, id2_);
    graph_.addDependency(id4_, id3_);
    graph_.addDependency(id5_, id4_);
  
    var to_ = new TopologicalOrder<>(graph_);
    
    var leaf_ = to_.cutLeaf();
    assertEquals("Node 1", leaf_.getData());
    assertFalse(to_.isEmpty());
    var empty_ = to_.cutLeaf();
    assertNull(empty_);
    
    /* -- close the Node 1 - Node 2 and 3 become leaves */
    leaf_.closeLeaf();
    
    /* -- open one of the leaves (it's not certain which one is returned) */
    leaf_ = to_.cutLeaf();
    assertTrue(
      Objects.equals(leaf_.getData(), "Node 2")
       || Objects.equals(leaf_.getData(), "Node 3"));
    assertFalse(to_.isEmpty());

    /* -- now modify the graph - add one new node and add dependencies
     *    from Node 5 to the new one, to a white existing, to a grey
     *    existing and to a black node. */
    to_.addNode(id6_, "Node 6");
    to_.addDependency(id5_, id6_); /* -- to the new one */
    to_.addDependency(id5_, id1_); /* -- to the black */
    to_.addDependency(id5_, id2_); /* -- to the white or grey */
    to_.addDependency(id5_, id3_); /* -- to the white or grey */

    /* -- repeat adding dependencies - nothing should happen */
    to_.addDependency(id5_, id6_);
    to_.addDependency(id5_, id1_);
    to_.addDependency(id5_, id2_);
    to_.addDependency(id5_, id3_);
    
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertTrue(
      Objects.equals(leaf_.getData(), "Node 2")
        || Objects.equals(leaf_.getData(), "Node 3")
        || Objects.equals(leaf_.getData(), "Node 6") );
    assertFalse(to_.isEmpty());
  
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertTrue(
      Objects.equals(leaf_.getData(), "Node 2")
        || Objects.equals(leaf_.getData(), "Node 3")
        || Objects.equals(leaf_.getData(), "Node 4")
        || Objects.equals(leaf_.getData(), "Node 6") );
    assertFalse(to_.isEmpty());
  
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertTrue(
      Objects.equals(leaf_.getData(), "Node 2")
        || Objects.equals(leaf_.getData(), "Node 3")
        || Objects.equals(leaf_.getData(), "Node 4")
        || Objects.equals(leaf_.getData(), "Node 6") );
    assertFalse(to_.isEmpty());
  
    /* -- last node must be Node 5 as all dependencies start in */
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertEquals("Node 5", leaf_.getData());
    assertFalse(to_.isEmpty());
    
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertNull(leaf_);
    assertTrue(to_.isEmpty());
  }
  
  @Test
  public void randomTest() throws DependencyCycleException {
    final int TEST_SIZE = 1000;
    
    Graph<String> graph_ = new GraphFull<>();

    /* -- generate node IDs and nodes */
    var ids_ = new ArrayList<ID>();
    for(int i_ = 0; i_ < TEST_SIZE; i_++) {
      var id_ = new ID(String.format("%06d", i_));
      ids_.add(id_);
      graph_.addNode(id_, String.format("Node %d", i_));
    }
    
    /* -- make base dependency line */
    for(int i_ = 1; i_ < TEST_SIZE; i_++) {
      graph_.addDependency(ids_.get(i_), ids_.get(i_ - 1));
    }

    /* -- generate random dependencies */
    var rnd_ = new Random();
    for(int i_ = 0; i_ < TEST_SIZE * TEST_SIZE; ++i_) {
      var i1_ = rnd_.nextInt(TEST_SIZE);
      var i2_ = rnd_.nextInt(TEST_SIZE);
      if(i1_ == i2_)
        continue;
      
      if(i1_ < i2_) {
        graph_.addDependency(ids_.get(i2_), ids_.get(i1_));
      }
      else {
        graph_.addDependency(ids_.get(i1_), ids_.get(i2_));
      }
    }
    
    /* -- now cut entire graph */
    var to_ = new TopologicalOrder<>(graph_);
    for(int i_ = 0; i_ < TEST_SIZE; ++i_) {
      var leaf_ = to_.cutLeaf();
      assertEquals(String.format("Node %d", i_), leaf_.getData());
      assertFalse(to_.isEmpty());
      assertNull(to_.cutLeaf());
      
      /* -- add some new dependency */
      var i1_ = rnd_.nextInt(TEST_SIZE);
      var i2_ = rnd_.nextInt(TEST_SIZE);
      if(i1_ != i2_) {
        if(i1_ > i2_) {
          var tmp_ = i1_;
          i1_ = i2_;
          i2_ = tmp_;
        }
        if(i2_ > i_) {
          to_.addDependency(ids_.get(i2_), ids_.get(i1_));
        }
      }
      
      leaf_.closeLeaf();
    }
    assertTrue(to_.isEmpty());
    assertNull(to_.cutLeaf());
  }
  
  @Test
  public void filteredGraph() throws DependencyCycleException {
    Graph<String> base_ = new GraphFull<>();
  
    var id1_ = new ID("1");
    var id2_ = new ID("2");
    var id3_ = new ID("3");
    var id4_ = new ID("4");
    var id5_ = new ID("5");
    var id6_ = new ID("6");
    var id7_ = new ID("7");
    var id8_ = new ID("8");
    var id9_ = new ID("9");
  
    base_.addNode(id1_, "Node 1");
    base_.addNode(id2_, "Node 2");
    base_.addNode(id3_, "Node 3");
    base_.addNode(id4_, "Node 4");
    base_.addNode(id5_, "Node 5");
    base_.addNode(id6_, "Node 6");
    base_.addNode(id7_, "Node 7");
    base_.addNode(id9_, "Node 9");
  
    base_.addDependency(id2_, id1_);
    base_.addDependency(id3_, id1_);
    base_.addDependency(id7_, id1_);
    base_.addDependency(id4_, id2_);
    base_.addDependency(id7_, id2_);
    base_.addDependency(id4_, id3_);
    base_.addDependency(id9_, id3_);
    base_.addDependency(id5_, id4_);
    base_.addDependency(id6_, id7_);
    
    var graph_ = new GraphFiltered<>(base_, Arrays.asList(id5_, id2_, id9_));
    
    /* -- append new node and dependency */
    graph_.addNode(id8_, "Node 8");
    graph_.addDependency(id8_, id3_);
    
    class DataCollector implements BiConsumer<ID, Graph.Node<String>> {
      public final Set<ID> visited = new HashSet<>();
  
      @Override
      public void accept(ID id_, Graph.Node<String> stringNode_) {
        visited.add(id_);
      }
    }

    /* -- base graph must contain all nodes */
    var collector_ = new DataCollector();
    base_.forEachNode(collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id1_, id2_, id3_, id4_, id5_, id6_, id7_, id8_, id9_});
    
    /* -- filtered graph doesn't contain 6 and 7 */
    collector_ = new DataCollector();
    graph_.forEachNode(collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id1_, id2_, id3_, id4_, id5_, id8_, id9_});

    /* -- node 1 */
    assertTrue(base_.containsNode(id1_));
    var node_ = base_.getNode(id1_);
    assertEquals(0, node_.getOutDegree());
    assertTrue(graph_.containsNode(id1_));
    node_ = graph_.getNode(id1_);
    assertEquals(0, node_.getOutDegree());
  
    collector_ = new DataCollector();
    base_.forEachPredecessor(id1_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id2_, id3_, id7_});
    collector_ = new DataCollector();
    graph_.forEachPredecessor(id1_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id2_, id3_});
  
    collector_ = new DataCollector();
    base_.forEachSuccessor(id1_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(new ID[]{});
    collector_ = new DataCollector();
    graph_.forEachSuccessor(id1_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(new ID[]{});
  
    /* -- node 2 */
    assertTrue(base_.containsNode(id2_));
    node_ = base_.getNode(id2_);
    assertEquals(1, node_.getOutDegree());
    assertTrue(graph_.containsNode(id2_));
    node_ = graph_.getNode(id2_);
    assertEquals(1, node_.getOutDegree());
  
    collector_ = new DataCollector();
    base_.forEachPredecessor(id2_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id4_, id7_});
    collector_ = new DataCollector();
    graph_.forEachPredecessor(id2_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id4_});
  
    collector_ = new DataCollector();
    base_.forEachSuccessor(id2_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(new ID[]{id1_});
    collector_ = new DataCollector();
    graph_.forEachSuccessor(id2_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(new ID[]{id1_});
  
    /* -- node 3 */
    assertTrue(base_.containsNode(id3_));
    node_ = base_.getNode(id3_);
    assertEquals(1, node_.getOutDegree());
    assertTrue(graph_.containsNode(id3_));
    node_ = graph_.getNode(id3_);
    assertEquals(1, node_.getOutDegree());
  
    collector_ = new DataCollector();
    base_.forEachPredecessor(id3_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id4_, id8_, id9_});
    collector_ = new DataCollector();
    graph_.forEachPredecessor(id3_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id4_, id8_, id9_});
  
    collector_ = new DataCollector();
    base_.forEachSuccessor(id3_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(new ID[]{id1_});
    collector_ = new DataCollector();
    graph_.forEachSuccessor(id3_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(new ID[]{id1_});
  
    /* -- node 4 */
    assertTrue(base_.containsNode(id4_));
    node_ = base_.getNode(id4_);
    assertEquals(2, node_.getOutDegree());
    assertTrue(graph_.containsNode(id4_));
    node_ = graph_.getNode(id4_);
    assertEquals(2, node_.getOutDegree());
  
    collector_ = new DataCollector();
    base_.forEachPredecessor(id4_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id5_});
    collector_ = new DataCollector();
    graph_.forEachPredecessor(id4_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id5_});
  
    collector_ = new DataCollector();
    base_.forEachSuccessor(id4_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id2_, id3_});
    collector_ = new DataCollector();
    graph_.forEachSuccessor(id4_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id2_, id3_});
    
    /* -- node 5 */
    assertTrue(base_.containsNode(id5_));
    node_ = base_.getNode(id5_);
    assertEquals(1, node_.getOutDegree());
    assertTrue(graph_.containsNode(id5_));
    node_ = graph_.getNode(id5_);
    assertEquals(1, node_.getOutDegree());
  
    collector_ = new DataCollector();
    base_.forEachPredecessor(id5_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{});
    collector_ = new DataCollector();
    graph_.forEachPredecessor(id5_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{});
  
    collector_ = new DataCollector();
    base_.forEachSuccessor(id5_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id4_});
    collector_ = new DataCollector();
    graph_.forEachSuccessor(id5_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id4_});
  
    /* -- node 6 */
    assertTrue(base_.containsNode(id6_));
    node_ = base_.getNode(id6_);
    assertEquals(1, node_.getOutDegree());
    assertFalse(graph_.containsNode(id6_));
  
    collector_ = new DataCollector();
    base_.forEachPredecessor(id6_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{});
  
    collector_ = new DataCollector();
    base_.forEachSuccessor(id6_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id7_});
    
    /* -- node 7 */
    assertTrue(base_.containsNode(id7_));
    node_ = base_.getNode(id7_);
    assertEquals(2, node_.getOutDegree());
    assertFalse(graph_.containsNode(id7_));
  
    collector_ = new DataCollector();
    base_.forEachPredecessor(id7_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id6_});
  
    collector_ = new DataCollector();
    base_.forEachSuccessor(id7_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id1_, id2_});
    
    /* -- node 8 */
    assertTrue(base_.containsNode(id8_));
    node_ = base_.getNode(id8_);
    assertEquals(1, node_.getOutDegree());
    assertTrue(graph_.containsNode(id8_));
    node_ = graph_.getNode(id8_);
    assertEquals(1, node_.getOutDegree());
  
    collector_ = new DataCollector();
    base_.forEachPredecessor(id8_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{});
    collector_ = new DataCollector();
    graph_.forEachPredecessor(id8_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{});
  
    collector_ = new DataCollector();
    base_.forEachSuccessor(id8_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id3_});
    collector_ = new DataCollector();
    graph_.forEachSuccessor(id8_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id3_});

    /* -- node 9 */
    assertTrue(base_.containsNode(id9_));
    node_ = base_.getNode(id9_);
    assertEquals(1, node_.getOutDegree());
    assertTrue(graph_.containsNode(id9_));
    node_ = graph_.getNode(id9_);
    assertEquals(1, node_.getOutDegree());
  
    collector_ = new DataCollector();
    base_.forEachPredecessor(id9_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{});
    collector_ = new DataCollector();
    graph_.forEachPredecessor(id9_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{});
  
    collector_ = new DataCollector();
    base_.forEachSuccessor(id9_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id3_});
    collector_ = new DataCollector();
    graph_.forEachSuccessor(id9_, collector_);
    assertThat(collector_.visited).containsExactlyElementsIn(
        new ID[]{id3_});
  
    /* -- now try computation of the topological order over the filtered
     *    graph */
    var to_ = new TopologicalOrder<>(graph_);
    assertFalse(to_.isEmpty());
  
    var leaf_ = to_.cutLeaf();
    assertEquals("Node 1", leaf_.getData());
    var empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertFalse(to_.isEmpty());
  
    leaf_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertThat(leaf_.getData()).isAnyOf("Node 2", "Node 3");
    var leaf2_ = to_.cutLeaf();
    assertThat(leaf2_.getData()).isAnyOf("Node 2", "Node 3");
    assertThat(leaf_.getData()).isNotEqualTo(leaf2_.getData());
    assertFalse(to_.isEmpty());
    
    leaf_.closeLeaf();
    leaf2_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertThat(leaf_.getData()).isAnyOf("Node 4", "Node 8", "Node 9");
    leaf2_ = to_.cutLeaf();
    assertThat(leaf2_.getData()).isAnyOf("Node 4", "Node 8", "Node 9");
    var leaf3_ = to_.cutLeaf();
    assertThat(leaf3_.getData()).isAnyOf("Node 4", "Node 8", "Node 9");
    assertThat(leaf_).isNotEqualTo(leaf2_);
    assertThat(leaf2_).isNotEqualTo(leaf3_);
    assertThat(leaf3_).isNotEqualTo(leaf_);
    assertFalse(to_.isEmpty());
    
    leaf_.closeLeaf();
    leaf2_.closeLeaf();
    leaf3_.closeLeaf();
    leaf_ = to_.cutLeaf();
    assertThat(leaf_.getData()).isEqualTo("Node 5");
    assertFalse(to_.isEmpty());
    
    leaf_.closeLeaf();
    empty_result_ = to_.cutLeaf();
    assertNull(empty_result_);
    assertTrue(to_.isEmpty());
  }
}
