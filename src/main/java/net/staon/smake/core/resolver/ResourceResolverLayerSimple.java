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

import net.staon.smake.core.execution.ResourceID;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of the resource resolver layer
 *
 * This implementation just keeps a list of registered resolvers.
 * Matching resolvers are found by simple sequential search.
 */
public class ResourceResolverLayerSimple implements ResourceResolverLayer {
  private static class Record {
    ResourceMask mask;
    String group;
    ResourceResolver resolver;
    
    public Record(
        ResourceMask mask_,
        String group_,
        ResourceResolver resolver_) {
      mask = mask_;
      group = group_;
      resolver = resolver_;
    }
  }

  final List<Record> resolvers = new ArrayList<>();
  
  private void doAddResourceResolver(
      ResourceMask mask_,
      String group_,
      ResourceResolver resolver_) {
    resolvers.add(new Record(mask_, group_, resolver_));
  }
  
  @Override
  public void addResourceResolver(
      ResourceMask mask_,
      ResourceResolver resolver_) {
    doAddResourceResolver(mask_, null, resolver_);
  }
  
  @Override
  public void addResourceResolver(
      ResourceMask mask_,
      String group_,
      ResourceResolver resolver_) {
    doAddResourceResolver(mask_, group_, resolver_);
  }
  
  private void doSearchResolvers(
      ResourceResolverGroups resolvers_,
      ResourceID resource_,
      String content_type_) {
    for(var resolver_ : resolvers) {
      /* -- check whether the resolver matches the resource */
      boolean matches_ = false;
      if(content_type_ != null) {
        matches_ = resolver_.mask.matchResource(resource_, content_type_);
      }
      else {
        matches_ = resolver_.mask.matchResource(resource_);
      }
      
      /* -- put the matching resolver into appropriate group */
      if(matches_) {
        resolvers_.appendResolver(resolver_.group, resolver_.resolver);
      }
    }
  }
  
  @Override
  public void searchResolvers(
      ResourceResolverGroups resolvers_,
      ResourceID resource_) {
    doSearchResolvers(resolvers_, resource_, null);
  }
  
  @Override
  public void searchResolvers(
      ResourceResolverGroups resolvers_,
      ResourceID resource_,
      String content_type_) {
    doSearchResolvers(resolvers_, resource_, content_type_);
  }
}
