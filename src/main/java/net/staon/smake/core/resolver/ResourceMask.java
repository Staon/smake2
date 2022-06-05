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

public class ResourceMask {
  private final Pattern type;
  private final Pattern path;
  private final Pattern content;
  
  /**
   * Ctor - pattern for file resources
   *
   * @param type_ Pattern for resource type
   * @param path_ Pattern for resource path
   * @param content_ Pattern  for content type
   */
  public ResourceMask(Pattern type_, Pattern path_, Pattern content_) {
    type = type_;
    path = path_;
    content = content_;
  }
  
  /**
   * Ctor - pattern for non-file resources
   *
   * @param type_ Pattern for resource type
   * @param path_ Pattern for path
   */
  public ResourceMask(Pattern type_, Pattern path_) {
    this(type_, path_, new PatternNull());
  }
  
  /**
   * Match a resource with the mask
   *
   * @param resource_ The resource
   * @param content_type_ Content type
   * @return True if the resource matches
   */
  boolean matchResource(ResourceID resource_, String content_type_) {
    return type.matches(resource_.getType())
        && path.matches(resource_.getPath().asString())
        && content.matches(content_type_);
  }
  
  /**
   * Match a resource with the mask
   *
   * @param resource_ The resource
   * @return True if the resource matches
   */
  boolean matchResource(ResourceID resource_) {
    return matchResource(resource_, null);
  }
}
