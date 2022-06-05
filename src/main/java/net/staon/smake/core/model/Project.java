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
 * Representation of one smake project
 */
public class Project implements ModelNode, ProjectPartContainer {
  private final String name;
  private final List<ProjectPart> children;
  
  
  /**
   * Ctor
   *
   * @param name_ Name of the project
   */
  public Project(String name_) {
    name = name_;
    children = new ArrayList<>();
  }
  
  /**
   * Get project name
   */
  public String getName() {
    return name;
  }
  
  @Override
  public void apply(Visitor visitor_) throws SMakeException {
    visitor_.visitProject(this);
  }

  @Override
  public void applyChildren(Visitor visitor_) throws SMakeException {
    for(var child_ : children) {
      child_.apply(visitor_);
    }
  }
  
  public void addChild(ProjectPart child_) {
    assert child_ != null;
    children.add(child_);
  }
}
