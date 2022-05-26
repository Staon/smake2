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

import net.staon.smake.core.dependencies.GraphFull;
import net.staon.smake.core.dependencies.TopologicalOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * An object keeping all project's resources and dependencies between them.
 */
public class ResourceMap implements ResourceMapManipulator {
  private final Map<ResourceID, Resource> resources;
  private final GraphFull<Resource> resource_deps;
  
  /**
   * Ctor
   */
  public ResourceMap() {
    resources = new HashMap<>();
    resource_deps = new GraphFull<>();
  }
  
  @Override
  public Resource getResource(ResourceID id_) {
    return resources.get(id_);
  }
  
  @Override
  public void addResource(Resource resource_) {
    var id_ = resource_.getID();
    assert !resources.containsKey(id_);
    resources.put(id_, resource_);
  }
  
  @Override
  public void addDependency(ResourceID from_, ResourceID to_) {
    resource_deps.addDependency(from_.asGraphID(), to_.asGraphID());
  }
  
  /**
   * Create calculator of the dependency topological order of stored resources
   */
  public TopologicalOrder<Resource> createTopologicalOrder() {
    return new TopologicalOrder<>(resource_deps);
  }
}
