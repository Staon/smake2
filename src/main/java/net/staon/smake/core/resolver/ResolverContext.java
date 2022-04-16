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

import net.staon.smake.core.execution.*;
import net.staon.smake.core.model.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Context of the resolving process
 */
public class ResolverContext {
  ResolverStack resolvers;
  private ResourceMapManipulator resource_map;
  private Project project;
  private Artefact artefact;
  private List<ProductReference> products;
  private Queue<Resource> resource_queue;
  
  /**
   * Ctor
   *
   * @param resolvers_ Configured resolvers
   * @param resource_map_ Resource map of the project
   */
  public ResolverContext(
      ResolverStack resolvers_,
      ResourceMapManipulator resource_map_) {
    assert resolvers_ != null && resource_map_ != null;
  
    resolvers = resolvers_;
    resource_map = resource_map_;
    
    /* -- current state of resolving */
    project = null;
    artefact = null;
    products = null;
    resource_queue = null;
  }
  
  private void insertResource(Resource resource_) {
    resource_queue.add(resource_);
    resource_map.addResource(resource_);
  }
  
  private void insertUniqueResource(Resource resource_)
      throws DuplicatedResourceException {
    if(resource_map.containsResource(resource_)) {
      throw new DuplicatedResourceException(project, resource_);
    }
    insertResource(resource_);
  }
  
  /**
   * Resolve a project
   *
   * @param project_ The project
   */
  public void resolveProject(Project project_) throws Throwable {
    assert project == null && project_ != null;
    
    try {
      project = project_;
      products = new ArrayList<>();
      resource_queue = new ArrayDeque<>();

      /* -- initialize by source resources */
      for(var artefact_ : project_.getArtefacts()) {
        openArtefact(artefact_);
        closeArtefact(artefact_);
      }
      
      /* -- insert all product resources */
      for(var ref_ : products) {
        insertUniqueResource(ref_.getResource());
      }
  
      /* -- resolve all resources */
      while(!resource_queue.isEmpty()) {
        var resource_ = resource_queue.poll();
        resolvers.resolveResource(this, resource_);
      }
    }
    finally {
      resource_queue = null;
      products = null;
      artefact = null;
      project = null;
    }
  }
  
  private void openArtefact(Artefact artefact_) throws Throwable {
    assert project != null && artefact == null && artefact_ != null;
    
    artefact = artefact_;
    resolvers.resolveArtefact(this, artefact);
  }
  
  /**
   * Add new artefact product
   *
   * @param product_type_ Type of the artefact product
   * @param product_resource_ The product
   */
  public void registerArtefactProduct(
      String product_type_,
      Resource product_resource_) throws DuplicatedResourceException {
    assert project != null && artefact != null;
  
    var product_ref_ = new ProductReference(
        artefact, product_type_, product_resource_);
  
    /* -- Postpone insertion of product resources after all sources.
     *    Hence, we can check that the production resource is not
     *    duplicated in sources. */
    products.add(product_ref_);
    
    /* -- now add all source files attached to the product resource */
    for(var source_ : artefact.getSources()) {
      var resource_ = new ResourceSource(source_);
    
      /* -- source resources may be shared by several artefacts or
       *    artefact products. */
      var actual_ = resource_map.getResource(resource_.getID());
      if(actual_ == null) {
        insertResource(resource_);
        actual_ = resource_;
      }
      actual_.attachWithProduct(product_ref_);
    }
  }
  
  private void closeArtefact(Artefact artefact_) {
    assert project != null && artefact != null && artefact == artefact_;
    artefact = null;
  }
  
  /**
   * Register new resource which is a result of a resource resolver
   *
   * @param resolved_ The resource which is resolved by the resolver
   * @param new_resource_ New resource which is the result of the resolver
   */
  public void registerResultResource(Resource resolved_, Resource new_resource_)
      throws DuplicatedResourceException {
    assert project != null && artefact == null;
    assert resolved_ != null && new_resource_ != null;
    
    insertUniqueResource(new_resource_);
    new_resource_.attachWithSameProduct(resolved_);
  }
}
