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

import java.util.*;

/**
 * List of applicable resource resolvers constructed for a resource
 */
public class ResourceResolverGroups implements Iterable<ResourceResolver> {
  private final Map<String, ResourceResolver> groups;
  private final List<ResourceResolver> singles;
  
  public ResourceResolverGroups() {
    groups = new HashMap<>();
    singles = new ArrayList<>();
  }
  
  /**
   * Append new resolver
   *
   * @param group_ The resolver group. Null or empty string means a single
   *     resolver.
   * @param resolver_ The resolver
   */
  public void appendResolver(String group_, ResourceResolver resolver_) {
    assert resolver_ != null;
    
    if(group_ == null || group_.isEmpty()) {
      singles.add(resolver_);
    }
    else {
      if(!groups.containsKey(group_)) {
        groups.put(group_, resolver_);
      }
    }
  }
  
  /**
   * Check whether the list of resolvers is empty
   */
  boolean isEmpty() {
    return groups.isEmpty() && singles.isEmpty();
  }

  private class ResolverIterator implements Iterator<ResourceResolver> {
    Iterator<ResourceResolver> groups_iter;
    Iterator<ResourceResolver> singles_iter;

    public ResolverIterator() {
      groups_iter = groups.values().iterator();
      singles_iter = singles.iterator();
    }
    
    @Override
    public boolean hasNext() {
      return groups_iter.hasNext() || singles_iter.hasNext();
    }
  
    @Override
    public ResourceResolver next() {
      if(groups_iter.hasNext())
        return groups_iter.next();
      if(singles_iter.hasNext())
        return singles_iter.next();
      throw new NoSuchElementException();
    }
  }
  
  @Override
  public Iterator<ResourceResolver> iterator() {
    return new ResolverIterator();
  }
}
