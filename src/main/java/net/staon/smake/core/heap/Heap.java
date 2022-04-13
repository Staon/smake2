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
