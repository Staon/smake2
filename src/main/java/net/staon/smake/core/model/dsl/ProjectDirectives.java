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
package net.staon.smake.core.model.dsl;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import net.staon.smake.core.model.ModelException;
import net.staon.smake.core.model.ProjectBuilder;

/**
 * DSL directives allows inside a project description
 */
public class ProjectDirectives {
  private final Context context;
  
  /**
   * Ctor
   *
   * @param context_ Parser context
   */
  public ProjectDirectives(
      Context context_) {
    context = context_;
  }
  
  public void artefact(
      String name_,
      String type_,
      @DelegatesTo(
          strategy = Closure.DELEGATE_ONLY,
          value = ArtefactDirectives.class)
      Closure body_) throws ModelReaderException {
    assert context.project != null;
    
    try {
      context.project.openArtefact(name_, type_);
      context.delegateDirectives(new ArtefactDirectives(context), this, body_);
      context.project.closeArtefact();
    }
    catch(ModelException exc_) {
      throw new ParseErrorException(exc_.getMessage());
    }
  }
  
  public void block(
      @DelegatesTo(
          strategy = Closure.DELEGATE_ONLY,
          value = ArtefactDirectives.class)
      Closure body_) throws ModelReaderException {
    assert context.project != null;
  
    context.project.openProjectBlock();
    context.delegateDirectives(new ProjectDirectives(context), this, body_);
    context.project.closeProjectBlock();
  }
}
