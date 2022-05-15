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
 * A resource representing a real file
 *
 * A file resource can be converted to filesystem path. And it keeps a content
 * type - e.g. C++ source, Fortran source etc.
 */
public interface ResourceFile extends Resource {
  /**
   * Get content type
   */
  public String getContentType();
  
  /* -- TODO: add the conversion to filesystem path */
  
  @Override
  default void apply(ResourceVisitor visitor_) {
    visitor_.visit(this);
  }
}
