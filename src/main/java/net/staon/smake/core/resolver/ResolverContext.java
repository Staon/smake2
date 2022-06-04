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

import java.util.ArrayList;
import java.util.List;

/**
 * Context of the resolving process
 */
public class ResolverContext {
  public static final String SOURCE_TYPE = "smake::source";
  public static final String TARGET_TYPE = "smake::target";
  public static final String UNKNOWN_CONTENT = "smake::unknown_content";
  
  Resolver resolvers;
  private ResourceMapManipulator resource_map;
  private Project project;
  private Artefact artefact;
  private ProductReference product_ref;
  private List<ProductReference> products;
  private ResourceQueue resource_queue;
  
  private class ResolverLayerGuard implements AutoCloseable {
    public ResolverLayerGuard(
        ResourceResolverLayer layer_) {
      resolvers.openResourceResolverLayer(layer_);
    }
  
    @Override
    public void close() throws Exception {
      resolvers.closeResourceResolverLayer();
    }
  }
  
  private class ResolverVisitor implements Visitor {
    @Override
    public void visitProject(Project project_) throws Throwable {
      assert project == null && project_ != null;
      
      try {
        project = project_;
        products = new ArrayList<>();
        resource_queue = new ResourceQueue();
        
        /* -- visit project children (mainly artefacts) */
        project_.applyChildren(this);
        
        /* -- insert all product resources */
        for(var ref_ : products) {
          insertUniqueResource(ref_.getResource());
        }
        
        /* -- resolve all resources */
        while(!resource_queue.isEmpty()) {
          var resource_ = resource_queue.popResource();
          resolvers.resolveResource(ResolverContext.this, resource_);
        }
      }
      finally {
        resource_queue = null;
        products = null;
        artefact = null;
        project = null;
      }
    }
    private ResolverVisitor visitor;
  
    @Override
    public void visitBlock(ProjectBlock block_) throws Throwable {
      try(var ignored = new ResolverLayerGuard(
          new ResourceResolverLayerSimple())) {
        block_.applyChildren(this);
      }
    }
  
    @Override
    public void visitArtefact(Artefact artefact_) throws Throwable {
      assert project != null && artefact == null && artefact_ != null;
      
      try {
        artefact = artefact_;
        
        /* -- resolve the artefact - children of the artefact are
         *    iterated for each artefact product. */
        resolvers.resolveArtefact(ResolverContext.this, artefact);
      }
      finally {
        artefact = null;
      }
      
    }

    @Override
    public void visitSource(Source source_) throws Throwable {
      assert project != null && artefact != null && product_ref != null;
      
      var resource_ = createSourceResource(source_.getPath());
      
      /* -- source resources may be shared by several artefacts or
       *    artefact products. */
      var actual_ = resource_map.getResource(resource_.getID());
      if(actual_ == null) {
        insertResource(resource_);
        actual_ = resource_;
      }
      /* -- TODO: attach to product */
    }
  }
  private final ResolverVisitor visitor;
  
  /**
   * Ctor
   *
   * @param resolvers_ Configured resolvers
   * @param resource_map_ Resource map of the project
   */
  public ResolverContext(
      Resolver resolvers_,
      ResourceMapManipulator resource_map_) {
    assert resolvers_ != null && resource_map_ != null;
  
    resolvers = resolvers_;
    resource_map = resource_map_;
    
    /* -- current state of resolving */
    project = null;
    artefact = null;
    product_ref = null;
    products = null;
    resource_queue = null;
    visitor = new ResolverVisitor();
  }
  
  private Resource createSourceResource(Path path_) {
    return new ResourcePhysical(SOURCE_TYPE, path_, UNKNOWN_CONTENT);
  }
  
  /**
   * Create new target physical resource (used by resolvers)
   *
   * @param path_ Path of the resource
   * @param content_type_ Content type of the target resource
   * @return The resource
   */
  public Resource createTargetResource(Path path_, String content_type_) {
    return new ResourcePhysical(TARGET_TYPE, path_, content_type_);
  }
  
  private void insertResource(Resource resource_) {
    resource_queue.pushResource(resource_);
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
    project_.apply(visitor);
  }
  
  /**
   * Add new artefact product
   *
   * @param product_type_ Type of the artefact product
   * @param product_resource_ The product
   */
  public void registerArtefactProduct(
      String product_type_,
      Resource product_resource_) throws Throwable {
    assert project != null && artefact != null && product_ref == null;
  
    try {
      product_ref = new ProductReference(
          artefact, product_type_, product_resource_);
  
      /* -- Postpone insertion of product resources after all sources.
       *    Hence, we can check that the production resource is not
       *    duplicated in sources. */
      products.add(product_ref);
  
      /* -- iterate artefact content */
      artefact.applyChildren(visitor);
    }
    finally {
      product_ref = null;
    }
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
    /* TODO: attach with the product resource */
  }
}
