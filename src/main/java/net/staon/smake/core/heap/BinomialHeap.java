package net.staon.smake.core.heap;

import java.util.Comparator;
import java.util.IdentityHashMap;

/**
 * Heap implemented by binomial trees
 *
 * @param <T> Type of data associated with heap items
 */
public class BinomialHeap<T> implements Heap<T> {
  private final Comparator<T> comparator;
  Node heap;
  IdentityHashMap<T, Node> handles;
  
  /**
   * Ctor
   *
   * @param comparator_ A comparator used for ordering of stored items
   */
  public BinomialHeap(Comparator<T> comparator_) {
    comparator = comparator_;
    handles = new IdentityHashMap<>();
  }
  
  public void insert(T item_) {
    assert handles.get(item_) == null;
    
    var node_ = new Node(item_);
    heap = mergeHeaps(heap, node_);
    handles.put(item_, node_);
  }
  
  public void update(T item_) {
    Node node_ = handles.get(item_);
    assert node_ != null;
  
    /* -- move the node into the root of its tree */
    handles.remove(item_);
    while(node_.parent != null) {
      node_.data = node_.parent.data;
      handles.replace(node_.data, node_);
      node_ = node_.parent;
    }
    node_.data = item_;
    handles.put(item_, node_);
  
    /* -- remove the root and merge the node back into the heap */
    Node following = node_.remove();
    if(node_ == heap)
      heap = following;
    heap = mergeHeaps(heap, node_.cutRoot());
    heap = mergeHeaps(heap, node_);
  }
  
  private Node findMinTree() {
    assert heap != null;
  
    /* -- find the minimal root */
    Node min_ = heap;
    Node tmp_ = heap.next;
    while(tmp_ != null) {
      if(comparator.compare(tmp_.data, min_.data) < 0) {
        min_ = tmp_;
      }
      tmp_ = tmp_.next;
    }
    return min_;
  }
  
  public T getMin() {
    return findMinTree().data;
  }
  
  public T pollMin() {
    assert heap != null;

    /* -- find the minimal root */
    Node min_ = findMinTree();
    
    /* -- remove the tree from the list */
    Node following_ = min_.remove();
    if(min_ == heap)
      heap = following_;
    handles.remove(min_.data);
    
    /* -- now split the searched binomial tree (remove the root) and
     *    merge children with the heap (the children are recursively
     *    binomial trees). */
    heap = mergeHeaps(heap, min_.cutRoot());
    
    return min_.data;
  }
  
  public boolean isEmpty() {
    return heap == null;
  }
  
  private Node mergeHeaps(Node first_, Node second_) {
    if(first_ == null)
      return second_;
    if(second_ == null)
      return first_;
    
    /* -- first pass: make list of trees sorted by tree orders */
    Node retval_ = null;
    Node curr_tail_ = null;
    while(first_ != null || second_ != null) {
      Node next_in_chain_;
      if(second_ == null) {
        next_in_chain_ = first_;
        first_ = first_.next;
      }
      else if(first_ == null) {
        next_in_chain_ = second_;
        second_ = second_.next;
      }
      else if(first_.order <= second_.order) {
        next_in_chain_ = first_;
        first_ = first_.next;
      }
      else {
        next_in_chain_ = second_;
        second_ = second_.next;
      }
  
      next_in_chain_.remove();
      if(curr_tail_ == null) {
        retval_ = curr_tail_ = next_in_chain_;
      }
      else {
        curr_tail_.insertAfter(next_in_chain_);
        curr_tail_ = next_in_chain_;
      }
    }

    /* -- second pass: merge trees with the same order */
    Node tmp_ = retval_;
    retval_ = null;
    curr_tail_ = null;
    while(tmp_ != null) {
      Node following_ = tmp_.next;
      Node next_in_chain_;
  
      if(following_ != null && tmp_.order == following_.order) {
        /* -- two adjacent trees has the same order, merge them */
        Node following2_ = following_.next;
        tmp_.remove();
        following_.remove();
        Node merged_ = tmp_.merge(comparator, following_);
        if(following2_ != null && following2_.order < merged_.order) {
          /* -- Three trees had the same order: insert the merged
           *    tree after the third one and insert the third tree
           *    into the output as it has lower order than the merged. */
          next_in_chain_ = following2_;
          following2_.insertAfter(merged_);
          tmp_ = merged_;
        }
        else {
          /* -- Third tree has the same or higher order as the merged one.
           *    Insert the merged tree at the beginning and repeat the cycle. */
          if(following2_ != null)
            following2_.insertBefore(merged_);
          tmp_ = merged_;
          continue;
        }
      }
      else {
        next_in_chain_ = tmp_;
        tmp_ = tmp_.next;
        next_in_chain_.remove();
      }
  
      if(curr_tail_ == null) {
        retval_ = curr_tail_ = next_in_chain_;
      }
      else {
        curr_tail_.insertAfter(next_in_chain_);
        curr_tail_ = next_in_chain_;
      }
    }
    
    return retval_;
  }
  
  private class Node {
    private int order;
    private Node parent;
    private Node children;
    private Node next;
    private Node prev;
    private T data;
    
    public Node(T data_) {
      order = 0;
      data = data_;
    }
    
    public void insertBefore(Node node2_) {
      if(prev != null) {
        prev.next = node2_;
      }
      node2_.prev = prev;
      node2_.next = this;
      prev = node2_;
    }
    
    public void insertAfter(Node node2_) {
      if(next != null) {
        next.prev = node2_;
      }
      node2_.next = next;
      node2_.prev = this;
      next = node2_;
    }
    
    public Node remove() {
      if(prev != null) {
        prev.next = next;
      }
      if(next != null) {
        next.prev = prev;
      }
      Node following_ = next;
      next = prev = null;
      return following_;
    }
    
    public Node merge(Comparator<T> comparator_, Node node2_) {
      assert parent == null && node2_.parent == null;
      
      if(comparator_.compare(this.data, node2_.data) <= 0) {
        ++this.order;
        if(children != null) {
          children.insertBefore(node2_);
        }
        children = node2_;
        node2_.parent = this;
        return this;
      }
      else {
        ++node2_.order;
        if(node2_.children != null) {
          node2_.children.insertBefore(this);
        }
        node2_.children = this;
        parent = node2_;
        return node2_;
      }
    }
    
    public Node cutRoot() {
      if(children == null)
        return null;
      Node curr_ = children;
      while(true) {
        curr_.parent = null;
        Node tmp_ = curr_.next;
        curr_.next = curr_.prev;
        curr_.prev = tmp_;
        if(curr_.prev == null)
          break;
        curr_ = curr_.prev;
      }
      order = 0;
      children = null;
      return curr_;
    }
  }
  
  private class Handle {}
}
