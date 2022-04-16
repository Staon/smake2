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
 * A target resource
 *
 * Smake's target resource is any resource which is a result of an execution
 * task. Typically, a target resource is a compiled object file, linked
 * executable or composed library archive. However, even generated language
 * source files are smake targets too.
 */
public class ResourceTarget extends Resource {
  private final Path path;
  
  /**
   * Ctor
   *
   * @param path_ The resource path
   */
  public ResourceTarget(Path path_) {
    assert path_ != null;
    path = path_;
  }
  
  @Override
  public String getType() {
    return "smake::target";
  }
  
  @Override
  public Path getPath() {
    return path;
  }
}
