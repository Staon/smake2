package net.staon.smake.core.heap;

import org.junit.jupiter.api.Test;

import javax.management.openmbean.ArrayType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BinomialHeapTest {
  private class Item {
    public int value;
    public Item(int value_) {
      value = value_;
    }
  }
  
  @Test
  public void increasingSequence() {
    Heap<Integer> heap_ = new BinomialHeap<>(Integer::compareTo);
    
    heap_.insert(1);
    heap_.insert(2);
    heap_.insert(3);
    heap_.insert(4);
    heap_.insert(5);
    heap_.insert(6);
  
    assertEquals(1, heap_.getMin());
    assertEquals(1, heap_.pollMin());
    assertEquals(2, heap_.getMin());
    assertEquals(2, heap_.pollMin());
    assertEquals(3, heap_.getMin());
    assertEquals(3, heap_.pollMin());
    assertEquals(4, heap_.getMin());
    assertEquals(4, heap_.pollMin());
    assertEquals(5, heap_.getMin());
    assertEquals(5, heap_.pollMin());
    assertEquals(6, heap_.getMin());
    assertEquals(6, heap_.pollMin());
  
    assertTrue(heap_.isEmpty());
  }
  
  @Test
  public void decreasingSequence() {
    Heap<Integer> heap_ = new BinomialHeap<>(Integer::compareTo);
    
    heap_.insert(6);
    heap_.insert(5);
    heap_.insert(4);
    heap_.insert(3);
    heap_.insert(2);
    heap_.insert(1);
  
    assertEquals(1, heap_.getMin());
    assertEquals(1, heap_.pollMin());
    assertEquals(2, heap_.getMin());
    assertEquals(2, heap_.pollMin());
    assertEquals(3, heap_.getMin());
    assertEquals(3, heap_.pollMin());
    assertEquals(4, heap_.getMin());
    assertEquals(4, heap_.pollMin());
    assertEquals(5, heap_.getMin());
    assertEquals(5, heap_.pollMin());
    assertEquals(6, heap_.getMin());
    assertEquals(6, heap_.pollMin());
  
    assertTrue(heap_.isEmpty());
  }
  
  @Test
  public void allTheSame() {
    Heap<String> heap_ = new BinomialHeap<>(String::compareTo);
    
    heap_.insert(new String("1"));
    heap_.insert(new String("1"));
    heap_.insert(new String("1"));
    heap_.insert(new String("1"));
    heap_.insert(new String("1"));
    heap_.insert(new String("1"));
  
    assertEquals("1", heap_.getMin());
    assertEquals("1", heap_.pollMin());
    assertEquals("1", heap_.getMin());
    assertEquals("1", heap_.pollMin());
    assertEquals("1", heap_.getMin());
    assertEquals("1", heap_.pollMin());
    assertEquals("1", heap_.getMin());
    assertEquals("1", heap_.pollMin());
    assertEquals("1", heap_.getMin());
    assertEquals("1", heap_.pollMin());
    assertEquals("1", heap_.getMin());
    assertEquals("1", heap_.pollMin());
  
    assertTrue(heap_.isEmpty());
  }
  
  @Test
  public void updateAllRevert() {
    Heap<Item> heap_ = new BinomialHeap<Item>(Comparator.comparingInt(i -> i.value));
    
    var i1_ = new Item(6);
    var i2_ = new Item(5);
    var i3_ = new Item(4);
    var i4_ = new Item(3);
    var i5_ = new Item(2);
    var i6_ = new Item(1);
    
    heap_.insert(i1_);
    heap_.insert(i2_);
    heap_.insert(i3_);
    heap_.insert(i4_);
    heap_.insert(i5_);
    heap_.insert(i6_);
    
    assertEquals(1, heap_.getMin().value);
    
    i1_.value = 1; heap_.update(i1_);
    i2_.value = 2; heap_.update(i2_);
    i3_.value = 3; heap_.update(i3_);
    i4_.value = 4; heap_.update(i4_);
    i5_.value = 5; heap_.update(i5_);
    i6_.value = 6; heap_.update(i6_);
  
    assertEquals(1, heap_.getMin().value);
    assertEquals(1, heap_.pollMin().value);
    assertEquals(2, heap_.getMin().value);
    assertEquals(2, heap_.pollMin().value);
    assertEquals(3, heap_.getMin().value);
    assertEquals(3, heap_.pollMin().value);
    assertEquals(4, heap_.getMin().value);
    assertEquals(4, heap_.pollMin().value);
    assertEquals(5, heap_.getMin().value);
    assertEquals(5, heap_.pollMin().value);
    assertEquals(6, heap_.getMin().value);
    assertEquals(6, heap_.pollMin().value);
  
    assertTrue(heap_.isEmpty());
  }
  
  @Test
  public void updateAllRevert2() {
    Heap<Item> heap_ = new BinomialHeap<Item>(Comparator.comparingInt(i -> i.value));
    
    var i1_ = new Item(1);
    var i2_ = new Item(2);
    var i3_ = new Item(3);
    var i4_ = new Item(4);
    var i5_ = new Item(5);
    var i6_ = new Item(6);
    var i7_ = new Item(7);
    
    heap_.insert(i1_);
    heap_.insert(i2_);
    heap_.insert(i3_);
    heap_.insert(i4_);
    heap_.insert(i5_);
    heap_.insert(i6_);
    heap_.insert(i7_);
    
    assertEquals(1, heap_.getMin().value);

    i1_.value = 6; heap_.update(i1_);
    i2_.value = 5; heap_.update(i2_);
    i3_.value = 4; heap_.update(i3_);
    i4_.value = 3; heap_.update(i4_);
    i5_.value = 2; heap_.update(i5_);
    i6_.value = 1; heap_.update(i6_);
    i7_.value = 0; heap_.update(i7_);
  
    assertEquals(0, heap_.getMin().value);
    assertEquals(0, heap_.pollMin().value);
    assertEquals(1, heap_.getMin().value);
    assertEquals(1, heap_.pollMin().value);
    assertEquals(2, heap_.getMin().value);
    assertEquals(2, heap_.pollMin().value);
    assertEquals(3, heap_.getMin().value);
    assertEquals(3, heap_.pollMin().value);
    assertEquals(4, heap_.getMin().value);
    assertEquals(4, heap_.pollMin().value);
    assertEquals(5, heap_.getMin().value);
    assertEquals(5, heap_.pollMin().value);
    assertEquals(6, heap_.getMin().value);
    assertEquals(6, heap_.pollMin().value);
  
    assertTrue(heap_.isEmpty());
  }
  
  @Test
  public void randomTest() {
    /* -- generate random items */
    var values_ = new ArrayList<Item>();
    var rnd_ = new Random();
    for(int i_ = 0; i_ < 10000; ++i_) {
      values_.add(new Item(rnd_.nextInt()));
    }
    
    /* -- insert the items into the heap */
    Heap<Item> heap_ = new BinomialHeap<Item>(Comparator.comparingInt(i -> i.value));
    for(var value_ : values_) {
      heap_.insert(value_);
    }
    
    /* -- now update each value */
    for(var value_ : values_) {
      value_.value = rnd_.nextInt();
      heap_.update(value_);
    }
    
    /* -- check ordering in the heap */
    values_.sort(Comparator.comparingInt(i -> i.value));
    for(var value_ : values_) {
      assertEquals(value_.value, heap_.getMin().value);
      assertEquals(value_.value, heap_.pollMin().value);
    }
  
    assertTrue(heap_.isEmpty());
  }
}