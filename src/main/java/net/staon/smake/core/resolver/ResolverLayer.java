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

import java.util.HashMap;
import java.util.Map;

/**
 * Resolver layer
 *
 * This is one layer of the smake's resolving system. The layer is
 * a product of smake's configuration or directives in project's SMakefile.
 *
 * The resolving system is responsible for transformation of a project
 * description to resource map containing resources and task builders.
 */
public class ResolverLayer {
  private final ResolverLayer parent;
  
  private final Map<String, ArtefactResolver> artefact_resolvers;
  private final ResourceResolverLayer resource_resolvers;
  
  /**
   * Ctor
   *
   * @param parent_ Parent resolver layer. It may be null if there is no parent.
   */
  private ResolverLayer(
      ResolverLayer parent_,
      ResourceResolverLayer resource_resolvers_) {
    parent = parent_;
    artefact_resolvers = new HashMap<>();
    resource_resolvers = resource_resolvers_;
  }
  
  /**
   * Create new layer which has origin in configuration
   *
   * @param parent_ Parent layer. Null if there is no parent.
   * @return The layer
   */
  public static ResolverLayer createConfigLayer(
      ResolverLayer parent_) {
    return new ResolverLayer(parent_, new ResourceResolverLayerSimple());
  }
  
  /**
   * Create new layer which has origin in SMakefile directives
   *
   * @param parent_ Parent layer. Null if there is no parent.
   * @return The layer
   */
  public static ResolverLayer createProjectLayer(
      ResolverLayer parent_) {
    return new ResolverLayer(parent_, new ResourceResolverLayerSimple());
  }
  
  /**
   * Get parent layer
   */
  ResolverLayer getParent() {
    return parent;
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
   * Find a resolver applicable for an artefact
   *
   * @param artefact_ The artefact
   * @return The resolver or null
   */
  public ArtefactResolver searchArtefactResolvers(
      Artefact artefact_) {
    /* -- my layer can resolve the artefact */
    var resolver_ = artefact_resolvers.get(artefact_.getType());
    if(resolver_ != null) {
      return resolver_;
    }

    /* -- give a chance to the parent layer */
    if(parent != null) {
      return parent.searchArtefactResolvers(artefact_);
    }

    /* -- there is no resolver for this artefact */
    return null;
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
    assert resolver_ != null;
    resource_resolvers.addResourceResolver(mask_, resolver_);
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
    assert resolver_ != null;
    resource_resolvers.addResourceResolver(mask_, group_, resolver_);
  }
  
  /**
   * Find resolvers applicable for a resource
   *
   * @param applicable_resolvers_ The list of applicable resolvers
   * @param resource_ The resource
   */
  public void searchResourceResolvers(
      ResourceResolverGroups applicable_resolvers_,
      Resource resource_) {
    /* -- search for applicable resolvers in my layer */
    resource_.apply(new ResourceVisitor() {
      @Override
      public void visit(Resource resource_) {
        resource_resolvers.searchResolvers(
            applicable_resolvers_, resource_.getID());
      }
  
      @Override
      public void visit(ResourceFile resource_) {
        resource_resolvers.searchResolvers(
            applicable_resolvers_,
            resource_.getID(),
            resource_.getContentType());
      }
    });
    
    /* -- give a chance to parents */
    if(parent != null)
      parent.searchResourceResolvers(applicable_resolvers_, resource_);
  }
}
