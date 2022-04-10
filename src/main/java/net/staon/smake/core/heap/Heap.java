package net.staon.smake.core.heap;

/**
 * Generic interface of a heap structure
 *
 * @param <T> Type of data stored in the heap
 */
public interface Heap<T> {
  /**
   * Insert new item into the heap
   *
   * The @a item_ is placed into the heap according to the comparator
   * provided in the heap's constructor. Identity of the inserted item
   * works as identifier for the update operation. Hence, never insert
   * exactly same object twice!
   *
   * @param item_ The inserted item
   */
  void insert(T item_);
  
  /**
   * Update existing item
   *
   * This method rearranges the heap according to new item content
   *
   * @param item_ The item with changed content
   */
  void update(T item_);
  
  /**
   * Get minimal value in the heap. The heap must not be empty!
   */
  T getMin();
  
  /**
   * Remove and return the minimal item. The heap must not be empty!
   *
   * @return The item
   */
  T pollMin();
  
  /**
   * Check if the heap is empty
   */
  boolean isEmpty();
}
