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
import net.staon.smake.core.execution.ResourceFile;
import net.staon.smake.core.execution.ResourceVisitor;
import net.staon.smake.core.model.Artefact;

import java.util.*;

/**
 * Stacks of configured resolvers
 */
public class Resolver {
  private final Map<String, ArtefactResolver> artefact_resolvers;
  private final Deque<ResourceResolverLayer> resource_resolvers;
  
  /**
   * Ctor
   */
  public Resolver() {
    artefact_resolvers = new HashMap<>();
    resource_resolvers = new ArrayDeque<>();
  }
  
  /**
   * Register an artefact resolver
   *
   * @param artefact_type_ Type of the artefact
   * @param resolver_ The resolver
   */
  public void addArtefactResolver(
      String artefact_type_,
      ArtefactResolver resolver_) {
    assert !artefact_resolvers.containsKey(artefact_type_);
    artefact_resolvers.put(artefact_type_, resolver_);
  }
  
  /**
   * Resolve an artefact
   *
   * @param context_ Resolver context
   * @param artefact_ The artefact
   */
  public void resolveArtefact(ResolverContext context_, Artefact artefact_)
      throws CannotResolveArtefactException {
    var resolver_ = artefact_resolvers.get(artefact_.getType());
    if(resolver_ == null)
      throw new CannotResolveArtefactException(artefact_);
    resolver_.resolveArtefact(context_, artefact_);
  }
  
  /**
   * Push new list of resource resolvers
   *
   * @param layer_ The pushed resource layer
   */
  public void openResourceResolverLayer(ResourceResolverLayer layer_) {
    resource_resolvers.push(layer_);
  }
  
  /**
   * Add new resolver into current resource resolver layer
   *
   * @param mask_ Resource mask of the resolver
   * @param resolver_ The resolver
   */
  public void addResourceResolver(
      ResourceMask mask_,
      ResourceResolver resolver_) {
    assert resolver_ != null && !resource_resolvers.isEmpty();
    resource_resolvers.peek().addResourceResolver(mask_, resolver_);
  }
  
  /**
   * Add new resolver into current resource resolver layer
   *
   * @param mask_ Resource mask of the resolver
   * @param group_ A group the resolver belongs to
   * @param resolver_ The resolver
   */
  public void addResourceResolver(
      ResourceMask mask_,
      String group_,
      ResourceResolver resolver_) {
    assert resolver_ != null && !resource_resolvers.isEmpty();
    resource_resolvers.peek().addResourceResolver(mask_, group_, resolver_);
  }
  
  /**
   * Close opened layer of resource resolvers
   */
  public void closeResourceResolverLayer() {
    assert !resource_resolvers.isEmpty();
    resource_resolvers.pop();
  }
  
  /**
   * Resolve a resource
   *
   * @param context_ Resolver context
   * @param resource_ The resource
   */
  public void resolveResource(ResolverContext context_, Resource resource_)
      throws CannotResolveResourceException {
    /* -- search for applicable resolvers */
    var applicable_resolvers_ = new ResourceResolverGroups();
    for(var resolver_layer_ : resource_resolvers) {
      class Visitor implements ResourceVisitor {
        @Override
        public void visit(Resource resource_) {
          resolver_layer_.searchResolvers(
              applicable_resolvers_, resource_.getID());
        }
    
        @Override
        public void visit(ResourceFile resource_) {
          resolver_layer_.searchResolvers(
              applicable_resolvers_,
              resource_.getID(),
              resource_.getContentType());
        }
      }
      resource_.apply(new Visitor());
    }
    
    /* -- the resource cannot be resolved (there is no applicable resolver) */
    if(applicable_resolvers_.isEmpty())
      throw new CannotResolveResourceException(resource_);
      
    /* -- apply all searched resolvers */
    for(var resolver_ : applicable_resolvers_) {
      resolver_.resolveResource(context_, resource_);
    }
  }
}
