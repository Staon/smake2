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
 * A source resource
 *
 * A source resource represents an existing file in the filesystem (e.g.
 * a C++ source file written by the programmer). Don't be confused with
 * language source file and smake source files - a generated language
 * source file is not the smake source file (it's a target file as it's
 * generated).
 */
public class ResourceSource extends Resource {
  private final Path path;
  
  /**
   * Ctor
   *
   * @param path_ Resource path
   */
  public ResourceSource(Path path_) {
    assert path_ != null;
    path = path_;
  }
  
  @Override
  public String getType() {
    return "smake::source";
  }
  
  @Override
  public Path getPath() {
    return path;
  }
}
