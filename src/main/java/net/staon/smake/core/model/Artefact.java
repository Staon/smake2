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

import java.util.ArrayList;
import java.util.List;

/**
 * Smake artefact (e.g. a library, an executable)
 */
public class Artefact {
  private final String name;
  private final String type;
  private final List<Path> sources;
  
  /**
   * Ctor
   *
   * @param name_ Name of the artefact
   * @param type_ Type of the artefact
   */
  public Artefact(String name_, String type_) {
    name = name_;
    type = type_;
    sources = new ArrayList<>();
  }
  
  /**
   * Get artefact name
   */
  public String getName() {
    return name;
  }
  
  /**
   * Get artefact type
   */
  public String getType() {
    return type;
  }
  
  /**
   * Append new artefact source
   *
   * @param path_ Path of the source relative to the project
   */
  public void addSource(Path path_) {
    assert path_ != null;
    sources.add(path_);
  }
  
  /**
   * Get list of sources
   */
  public Iterable<Path> getSources() {
    return sources;
  }
}
