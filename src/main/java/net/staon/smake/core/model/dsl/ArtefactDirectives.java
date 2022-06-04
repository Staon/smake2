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

import net.staon.smake.core.model.InvalidPathException;
import net.staon.smake.core.model.Path;
import net.staon.smake.core.model.ProjectBuilder;

/**
 * Directives available in the artefact block
 */
public class ArtefactDirectives {
  private final Context context;
  
  /**
   * Ctor
   *
   * @param context_ Parser context
   */
  public ArtefactDirectives(Context context_) {
    context = context_;
  }
  
  /**
   * List of artefact sources
   *
   * @param sources_ List of paths of the artefact source resources
   */
  public void sources(String... sources_) throws ModelReaderException {
    try {
      for(var source_ : sources_) {
        context.project.addSource(new Path(source_));
      }
    }
    catch(Exception exc_) {
      throw new ParseErrorException(exc_.getMessage());
    }
  }
}
