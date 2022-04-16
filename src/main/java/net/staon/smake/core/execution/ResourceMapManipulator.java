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
package net.staon.smake.core.execution;

/**
 * Interface manipulating a resource map
 */
public interface ResourceMapManipulator {
  /**
   * Get a resource
   *
   * @param id_ ID of the resource
   * @return The resource or null if the resource is not present in the resource
   *     map.
   */
  Resource getResource(ResourceID id_);
  
  /**
   * Check whether a resource is stored in the resource map
   */
  default boolean containsResource(ResourceID id_) {
    return getResource(id_) != null;
  }
  
  /**
   * Check whether a resource is stored in the resource map
   */
  default boolean containsResource(Resource resource_) {
    return containsResource(resource_.getID());
  }

  /**
   * Add new resource into the resource map
   *
   * @param resource_ The new resource. It must not exist yet!
   */
  void addResource(Resource resource_);
  
  /**
   * Add a dependency between two resources
   */
  void addDependency(ResourceID from_, ResourceID to_);
  
  /**
   * Add a dependency between two resources
   */
  default void addDependency(Resource from_, Resource to_) {
    addDependency(from_.getID(), to_.getID());
  }
}
