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
import net.staon.smake.core.exception.ParseErrorException;
import net.staon.smake.core.model.ProjectBuilder;

/**
 * Directives of the smake DSL files
 */
public class RootDirectives {
  private final Context context;
  
  public RootDirectives(Context context_) {
    context = context_;
  }
  
  public void project(
      String name_,
      @DelegatesTo(
          strategy = Closure.DELEGATE_ONLY,
          value = ProjectDirectives.class)
      Closure body_) throws ParseErrorException {
    /* -- create the project builder */
    if(context.project != null) {
      throw new ParseErrorException("Only one project per SMakefile is supported!");
    }
    context.project = new ProjectBuilder(name_);

    /* -- delegate to project's directives */
    context.delegateDirectives(new ProjectDirectives(context), this, body_);
  }
}
