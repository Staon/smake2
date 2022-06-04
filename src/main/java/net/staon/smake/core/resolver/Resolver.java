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

import net.staon.smake.core.model.Artefact;

import java.util.*;

/**
 * Helper class implementing resolver algorithms
 */
public final class Resolver {
  /**
   * Resolve an artefact
   *
   * @param context_ Resolver context
   * @param top_layer_ Top resolver layer attached to the artefact
   * @param artefact_ The artefact
   */
  public static void resolveArtefact(
      ResolverContext context_,
      ResolverLayer top_layer_,
      Artefact artefact_)
      throws CannotResolveArtefactException {
    var resolver_ = top_layer_.searchArtefactResolvers(artefact_);
    if(resolver_ == null)
      throw new CannotResolveArtefactException(artefact_);
    resolver_.resolveArtefact(context_, artefact_);
  }
  
  /**
   * Resolve a resource
   *
   * @param context_ Resolver context
   * @param resource_ The resource
   */
  public static void resolveResource(
      ResolverContext context_,
      ResolverResource resource_)
      throws CannotResolveResourceException {
    
     
    /* -- search for applicable resolvers */
    var applicable_resolvers_ = new ResourceResolverGroups();
    resource_.getResolverStack().searchResourceResolvers(
        applicable_resolvers_,
        resource_.getResource());
    
    /* -- the resource cannot be resolved (there is no applicable resolver) */
    if(applicable_resolvers_.isEmpty())
      throw new CannotResolveResourceException(resource_.getResource());
      
    /* -- apply all searched resolvers */
    for(var resolver_ : applicable_resolvers_) {
      resolver_.resolveResource(context_, resource_.getResource());
    }
  }
}
