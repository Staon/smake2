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

/**
 * Visitor of the model structure
 */
public interface Visitor {
  /**
   * Visit Project node
   */
  default void visitProject(Project project_) throws SMakeException {
  
  }
  
  /**
   * Visit block node
   */
  default void visitBlock(ProjectBlock block_) throws SMakeException {
  
  }
  
  /**
   * Visit artefact node
   */
  default void visitArtefact(Artefact artefact_) throws SMakeException {
  
  }
  
  /**
   * Visit artefact source
   */
  default void visitSource(Source source_) throws SMakeException {
  
  }
}
