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
package net.staon.smake.core.resolver;

import net.staon.smake.core.model.Artefact;

/**
 * Interface of artefact resolver
 */
public interface ArtefactResolver {
  /**
   * Resolve an artefact
   *
   * This method must create all needed product resources according to
   * artefact type and name.
   *
   * @param context_ Resolver context
   * @param artefact_ The artefact
   * @return True if the artefact has been resolved.
   */
  boolean resolveArtefact(ResolverContext context_, Artefact artefact_);
}
