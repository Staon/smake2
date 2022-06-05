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

import net.staon.smake.core.exception.SMakeException;

import java.util.ArrayList;
import java.util.List;

/**
 * Smake artefact (e.g. a library, an executable)
 */
public class Artefact implements ProjectPart {
  private final String name;
  private final String type;
  private final List<Source> sources;
  
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
   * @param source_ The source
   */
  public void addSource(Source source_) {
    assert source_ != null;
    sources.add(source_);
  }
  
  @Override
  public void apply(Visitor visitor_) throws SMakeException {
    visitor_.visitArtefact(this);
  }
  
  @Override
  public void applyChildren(Visitor visitor_) throws SMakeException {
    for(var source_ : sources) {
      source_.apply(visitor_);
    }
  }
}
