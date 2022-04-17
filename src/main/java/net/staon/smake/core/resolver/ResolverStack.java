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
import net.staon.smake.core.model.Artefact;

import java.util.*;

/**
 * Stacks of configured resolvers
 */
public class ResolverStack {
  
  private final Deque<List<ArtefactResolver>> artefact_resolvers;
  private final Deque<List<ResourceResolver>> resource_resolvers;
  
  /**
   * Ctor
   */
  public ResolverStack() {
    artefact_resolvers = new ArrayDeque<>();
    resource_resolvers = new ArrayDeque<>();
  }
  
  /**
   * Push new list of artefact resolvers
   */
  public void openArtefactResolverLayer() {
    artefact_resolvers.push(new ArrayList<>());
  }
  
  /**
   * Add new resolver into current layer of artefact resolvers
   *
   * @param resolver_ The resolver
   */
  public void addArtefactResolver(ArtefactResolver resolver_) {
    assert resolver_ != null && !artefact_resolvers.isEmpty();
    artefact_resolvers.peek().add(resolver_);
  }
  
  /**
   * Close opened layer of artefact resolvers
   */
  public void closeArtefactResolverLayer() {
    assert !artefact_resolvers.isEmpty();
    artefact_resolvers.pop();
  }
  
  /**
   * Push new list of resource resolvers
   */
  public void openResourceResolverLayer() {
    resource_resolvers.push(new ArrayList<>());
  }
  
  /**
   * Add new resolver into current resource resolver layer
   */
  public void addResourceResolver(ResourceResolver resolver_) {
    assert resolver_ != null && !resource_resolvers.isEmpty();
    resource_resolvers.peek().add(resolver_);
  }
  
  /**
   * Close opened layer of resource resolvers
   */
  public void closeResourceResolverLayer() {
    assert !resource_resolvers.isEmpty();
    resource_resolvers.pop();
  }
  
  /**
   * Resolve an artefact
   *
   * @param context_ Resolver context
   * @param artefact_ The artefact
   */
  public void resolveArtefact(ResolverContext context_, Artefact artefact_)
      throws CannotResolveArtefactException {
    for(var resolver_list_ : artefact_resolvers) {
      for(var resolver_ : resolver_list_) {
        if(resolver_.resolveArtefact(context_, artefact_)) {
          return;
        }
      }
    }
    throw new CannotResolveArtefactException(artefact_);
  }
  
  /**
   * Resolve a resource
   *
   * @param context_ Resolver context
   * @param resource_ The resource
   */
  public void resolveResource(ResolverContext context_, Resource resource_)
      throws CannotResolveResourceException {
    for(var resolver_layer_ : resource_resolvers) {
      for(var resolver_ : resolver_layer_) {
        if(resolver_.resolveResource(context_, resource_)) {
          return;
        }
      }
    }
    throw new CannotResolveResourceException(resource_);
  }
}
