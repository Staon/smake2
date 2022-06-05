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
 * Representation of a block inside a project
 *
 * Block is a new layer of resolver context - new layer of resolvers, new
 * layer of compilation profiles. A block is automatically opened for
 * subdirectory, but it can be opened manually in the SMakeFile.
 */
public class ProjectBlock implements ProjectPart, ProjectPartContainer {
  private final List<ProjectPart> children;
  
  /**
   * Ctor
   */
  public ProjectBlock() {
    children = new ArrayList<>();
  }
  
  @Override
  public void apply(Visitor visitor_) throws SMakeException {
    visitor_.visitBlock(this);
  }
  
  @Override
  public void applyChildren(Visitor visitor_) throws SMakeException {
    for(var child_ : children) {
      child_.apply(visitor_);
    }
  }
  
  @Override
  public void addChild(ProjectPart child_) {
    children.add(child_);
  }
}
