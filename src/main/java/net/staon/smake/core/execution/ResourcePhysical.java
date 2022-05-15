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

import net.staon.smake.core.model.Path;

/**
 * Implementation of a file resource
 */
public class ResourcePhysical implements ResourceFile {
  private final String resource_type;
  private final Path resource_path;
  private final String content_type;
  
  /**
   * Ctor
   *
   * @param type_ Type of the resource (e.g. smake::source, smake::target)
   * @param path_ Smake path of the resource
   * @param content_ Content type of the resource (e.g. smake::c++)
   */
  public ResourcePhysical(String type_, Path path_, String content_) {
    resource_type = type_;
    resource_path = path_;
    content_type = content_;
  }
  
  @Override
  public final ResourceID getID() {
    return new ResourceID(resource_type, resource_path);
  }
  
  @Override
  public final String getContentType() {
    return content_type;
  }
}
