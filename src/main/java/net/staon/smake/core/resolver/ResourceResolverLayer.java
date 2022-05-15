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

/**
 * Resolver layer
 *
 * This is one layer of resource resolvers. I expect at least two
 * implementations: one simple for layers with small number of resolvers
 * (layers created for projects, nested blocks and artifacts) and second
 * implementation for global configuration containing (a guess) hundreds
 * of resolvers.
 */
public interface ResourceResolverLayer {
  /**
   * Append new resource resolver
   *
   * @param mask_ Resource mask (regex for type, path and content type)
   * @param resolver_ The registered resolver
   */
  void addResourceResolver(ResourceMask mask_, ResourceResolver resolver_);
  
  /**
   * Append new resource resolver
   *
   * @param mask_ Resource mask
   * @param group_ Resolver group the resolver belongs to
   * @param resolver_ The resolver
   */
  void addResourceResolver(
      ResourceMask mask_,
      String group_,
      ResourceResolver resolver_);
  
  /**
   * Search for resolvers applicable on a resource
   *
   * @param resolvers_ The applicable resolvers are filled in this container
   * @param resource_ The resource
   */
  void searchResolvers(
      ResourceResolverGroups resolvers_,
      ResourceID resource_);
  
  /**
   * Search for resolvers applicable on a resource
   *
   * @param resolvers_ The applicable resolvers are filled in this container
   * @param resource_ The resource
   * @param content_type_ Content type of the file resource
   */
  void searchResolvers(
      ResourceResolverGroups resolvers_,
      ResourceID resource_,
      String content_type_);
}
