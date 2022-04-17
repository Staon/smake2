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
package net.staon.smake.core.model;

/**
 * Source file of an artifact
 */
public class Source implements ModelNode {
  private final Path path;
  
  /**
   * Ctor
   *
   * @param path_ Source path relative to the project root
   */
  public Source(Path path_) {
    assert path_ != null;
    path = path_;
  }
  
  /**
   * Get source path
   */
  public Path getPath() {
    return path;
  }
  
  @Override
  public void apply(Visitor visitor_) throws Throwable {
    visitor_.visitSource(this);
  }
  
  @Override
  public void applyChildren(Visitor visitor_) throws Throwable {
    /* -- nothing to do, no children */
  }
}
