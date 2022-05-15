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

import net.staon.smake.core.execution.Resource;
import net.staon.smake.core.execution.ResourceID;
import net.staon.smake.core.model.Path;

import java.util.regex.Pattern;

public class ResourceMask {
  private final Pattern type;
  private final Pattern path;
  private final Pattern content;
  
  /**
   * Ctor
   *
   * @param type_ Regular expression for resource type
   * @param path_ Regular expression for resource path
   * @param content_ Regular expression for content type
   */
  public ResourceMask(String type_, String path_, String content_) {
    type = Pattern.compile(type_);
    path = Pattern.compile(path_);
    content = Pattern.compile(content_);
  }
  
  /**
   * Ctor - the content type doesn't matter
   *
   * @param type_ Regular expression for resource type
   * @param path_ Regular expression for resource path
   */
  public ResourceMask(String type_, String path_) {
    this(type_, path_, ".*");
  }
  
  /**
   * Match a resource with the mask
   *
   * @param resource_ The resource
   * @param content_type_ Content type
   * @return True if the resource matches
   */
  boolean matchResource(ResourceID resource_, String content_type_) {
    return type.matcher(resource_.getType()).matches()
        && path.matcher(resource_.getPath().asString()).matches()
        && content.matcher(content_type_).matches();
  }
  
  /**
   * Match a resource with the mask
   *
   * @param resource_ The resource
   * @return True if the resource matches
   */
  boolean matchResource(ResourceID resource_) {
    return matchResource(resource_, "");
  }
}
