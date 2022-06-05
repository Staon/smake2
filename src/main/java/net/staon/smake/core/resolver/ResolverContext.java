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

import net.staon.smake.core.exception.DuplicatedResourceException;
import net.staon.smake.core.exception.SMakeException;
import net.staon.smake.core.execution.*;
import net.staon.smake.core.model.*;

/**
 * Context of the resolving process
 */
public class ResolverContext {
  public static final String SOURCE_TYPE = "smake::source";
  public static final String TARGET_TYPE = "smake::target";
  public static final String UNKNOWN_CONTENT = "smake::unknown_content";
  
  /* -- current state of the resolution */
  static class ResolutionState {
    public ResolverLayer resolver_stack; /* -- current resolver stack */
    public Project project;              /* -- current project */
    public Artefact artefact;            /* -- current artefact */
    public ProductSpec product;          /* -- current product */
    public ResourceQueue resource_queue; /* -- queue of resources to be resolved */
    
    public ResolutionState(ResolverLayer resolver_stack_) {
      resolver_stack = resolver_stack_;
      project = null;
      artefact = null;
      product = null;
      resource_queue = null;
    }
    
    public void openProject(Project project_) {
      assert project == null && artefact == null && product == null;
      assert project_ != null;
      
      resolver_stack = ResolverLayer.createProjectLayer(resolver_stack);
      project = project_;
      artefact = null;
      product = null;
      resource_queue = new ResourceQueue();
    }
    
    public void openProjectBlock() {
      assert project != null && artefact == null && product == null;
      resolver_stack = ResolverLayer.createProjectLayer(resolver_stack);
    }
    
    public void openArtefact(Artefact artefact_) {
      assert project != null && artefact == null && product == null;
      assert artefact_ != null;

      artefact = artefact_;
    }
    
    public void openArtefactProduct(ProductSpec product_) {
      assert project != null && artefact != null && product == null;
      assert product_ != null;
      
      product = product_;
    }
    
    public void checkStateForResource() {
      assert project != null && artefact != null && product != null;
    }
    
    public void closeArtefactProduct() {
      product = null;
    }
    
    public void closeArtefact() {
      artefact = null;
    }
    
    public void closeProjectBlock() {
      resolver_stack = resolver_stack.getParent();
    }
    
    public void closeProject() {
      resource_queue = null;
      artefact = null;
      project = null;
      resolver_stack = resolver_stack.getParent();
    }
  }
  private final ResolutionState state;
  
  /* -- constructed resource map */
  private final ResourceMapManipulator resource_map;
  
  private static class StateGuard implements AutoCloseable {
    private final Runnable clean_up;
    
    public StateGuard(Runnable clean_up_) {
      clean_up = clean_up_;
    }

    @Override
    public void close() throws SMakeException {
      clean_up.run();
    }
  }
  
  private class ResolverVisitor implements Visitor {
    @Override
    public void visitProject(Project project_) throws SMakeException {
      state.openProject(project_);
      try(var ignored = new StateGuard(state::closeProject)) {
        /* -- visit project children (mainly artefacts) */
        project_.applyChildren(this);
  
        /* -- resolve all resources */
        while(!state.resource_queue.isEmpty()) {
          var resource_ = state.resource_queue.popResource();
          Resolver.resolveResource(ResolverContext.this, resource_);
        }
      }
    }
  
    @Override
    public void visitBlock(ProjectBlock block_) throws SMakeException {
      state.openProjectBlock();
      try(var ignored = new StateGuard(state::closeProjectBlock)) {
        block_.applyChildren(this);
      }
    }
  
    @Override
    public void visitArtefact(Artefact artefact_) throws SMakeException {
      state.openArtefact(artefact_);
      try(var ignored = new StateGuard(state::closeArtefact)) {
        /* -- resolve the artefact - children of the artefact are
         *    iterated for each artefact product. */
        Resolver.resolveArtefact(ResolverContext.this, state.resolver_stack, artefact_);
      }
    }

    @Override
    public void visitSource(Source source_) throws SMakeException {
      var resource_ = createSourceResource(source_.getPath());
      registerSharedResource(resource_);
    }
  }
  private final ResolverVisitor visitor;
  
  /**
   * Ctor
   *
   * @param resolver_stack_ Initial stack of resolvers (new layers
   *     will be pushed for projects, blocks and artefacts)
   * @param resource_map_ Resource map of the project
   */
  public ResolverContext(
      ResolverLayer resolver_stack_,
      ResourceMapManipulator resource_map_) {
    assert resource_map_ != null;

    state = new ResolutionState(resolver_stack_);
    resource_map = resource_map_;
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
  
  private void queueResource(Resource resource_) {
    state.resource_queue.pushResource(
        new ResolverResource(resource_, state.resolver_stack, state.product));
  }
  private void insertSharedResource(Resource resource_) {
    var actual_ = resource_map.getResource(resource_.getID());
    if(actual_ == null) {
      actual_ = resource_;
      resource_map.addResource(actual_);
    }
    queueResource(actual_);
  }
  
  private void insertUniqueResource(Resource resource_)
      throws DuplicatedResourceException {
    if(resource_map.containsResource(resource_)) {
      throw new DuplicatedResourceException(state.project, resource_);
    }
    resource_map.addResource(resource_);
    queueResource(resource_);
  }
  
  /**
   * Resolve a project
   *
   * @param project_ The project
   */
  public void resolveProject(Project project_) throws SMakeException {
    project_.apply(visitor);
  }
  
  /**
   * Add new artefact product
   *
   * The method registers new unique product resource and evaluates
   * all children of current artefact.
   *
   * @param product_ The artefact product
   */
  public void registerArtefactProduct(
      ProductSpec product_) throws Throwable {
    /* -- The product resource must be unique - there is no sense
    *     having shared products. */
    insertUniqueResource(product_.getProductResource());

    /* -- Evaluate all children. As each type of product can create
     *    different resources, all children are evaluated repeatedly
     *    for each product. */
    state.openArtefactProduct(product_);
    try(var ignored = new StateGuard(state::closeArtefactProduct)) {
      
      /* -- resolve all artefact sources against the new product */
      state.artefact.applyChildren(visitor);
    }
  }
  
  /**
   * Register new resource which is a result of a resource resolver
   * and must be unique.
   *
   * @param resource_ The registered resource
   */
  public void registerUniqueResource(Resource resource_)
      throws DuplicatedResourceException {
    state.checkStateForResource();
    insertUniqueResource(resource_);
  }
  
  /**
   * Register a resource which may be shared by several products or artefacts
   *
   * @param resource_ The resource
   */
  public void registerSharedResource(Resource resource_) {
    state.checkStateForResource();
    insertSharedResource(resource_);
  }
}
