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
package net.staon.smake.core.resolver;

import net.staon.smake.core.execution.Resource;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

/**
 * Resource queue
 */
public class ResourceQueue {
  private final Queue<ResolverResource> queue;
  
  /**
   * Ctor - empty queue
   */
  public ResourceQueue() {
    queue = new ArrayDeque<>();
  }
  
  /**
   * Ctor - initial content
   */
  public ResourceQueue(Collection<ResolverResource> init_) {
    queue = new ArrayDeque<>(init_);
  }
  
  /**
   * Check emptiness of the queue
   */
  public boolean isEmpty() {
    return queue.isEmpty();
  }
  
  /**
   * Insert resource into the queue
   */
  public void pushResource(ResolverResource resource_) {
    queue.add(resource_);
  }
  
  /**
   * Remove resource from the head
   *
   * @return The removed resource
   */
  public ResolverResource popResource() {
    assert !queue.isEmpty();
    return queue.poll();
  }
}
