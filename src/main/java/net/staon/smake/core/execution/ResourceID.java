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

import net.staon.smake.core.dependencies.ID;
import net.staon.smake.core.model.Path;

import java.util.Objects;

/**
 * Identifier of an smake resource
 */
public final class ResourceID {
  private final String type;
  private final Path path;
  
  /**
   * Ctor
   */
  public ResourceID(String type_, Path path_) {
    assert type_ != null && path_ != null;
    
    type = type_;
    path = path_;
  }
  
  /**
   * Copy ctor
   */
  public ResourceID(ResourceID id_) {
    type = id_.type;
    path = id_.path;
  }
  
  /**
   * Get resource type
   */
  public String getType() {
    return type;
  }
  
  /**
   * Get resource path
   */
  public Path getPath() {
    return path;
  }
  
  /**
   * Create graph ID from the resource ID
   */
  public ID asGraphID() {
    return new ID(type + '@' + path.asString());
  }
  
  @Override
  public boolean equals(Object o_) {
    if(this == o_) return true;
    if(o_ == null || getClass() != o_.getClass()) return false;
    ResourceID that = (ResourceID) o_;
    return type.equals(that.type) && path.equals(that.path);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(type, path);
  }
  
  @Override
  public String toString() {
    return "Resource(" + type + ", " + path + ')';
  }
}
