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
package net.staon.smake.core.toolchain;

import net.staon.smake.core.exception.SMakeException;
import net.staon.smake.core.resolver.ResolverLayer;

/**
 * A toolchain object
 *
 * A toolchain object is a holder of configuration. Its main responsibility
 * is to create resolver records.
 */
public interface Toolchain {
  /**
   * Create resolvers in specified layer
   *
   * @param layer_ The layer
   */
  void constructResolvers(ResolverLayer layer_) throws SMakeException;
}
