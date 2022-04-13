package net.staon.smake.core.graph.tests;

import net.staon.smake.core.dependencies.DependencyCycleException;
import net.staon.smake.core.dependencies.Graph;
import net.staon.smake.core.dependencies.ID;
import net.staon.smake.core.dependencies.TopologicalOrder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
  @Test
  public void topologicalOrderSimple() throws DependencyCycleException {
    var graph_ = new Graph<String>();
    
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
    var graph_ = new Graph<String>();
    
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
    var graph_ = new Graph<String>();
  
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
    var graph_ = new Graph<String>();
  
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
    
    var graph_ = new Graph<String>();

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
}
