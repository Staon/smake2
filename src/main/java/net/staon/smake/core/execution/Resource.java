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
 * Generic smake resource
 *
 * A resource is an abstraction representing mostly source and generated
 * files. However, they can represent just a virtual resources, e.g.
 * exported and imported files (they are just project interface, physical
 * file is represented by another resource).
 *
 * A resource is uniquely identified by the pair (type, path). That's it,
 * there can be resources with the same path but different type. E.g.
 * exported and physical resources can have the same path.
 *
 * A note: generic resource is not attached to an artefact as imported
 * and exported resources are global for entire project (e.g. headers are
 * installed for entire project).
 */
public interface Resource {
  /**
   * Get ID of the resource (pair [resource type, resource path])
   */
  ResourceID getID();
  
  /**
   * Apply a visitor on this resource
   */
  default void apply(ResourceVisitor visitor_) {
    visitor_.visit(this);
  }
}
