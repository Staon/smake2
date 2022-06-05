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

import java.util.HashMap;
import java.util.Map;

/**
 * Group of toolchains
 */
public class ToolchainGroup implements Toolchain {
  private final Map<String, Toolchain> toolchains;
  
  /**
   * Ctor
   */
  public ToolchainGroup() {
    toolchains = new HashMap<>();
  }
  
  /**
   * Insert a toolchain
   *
   * The method inserts or replaces a toolchain mapped with @a id_.
   *
   * @param id_ ID of the toolchain
   * @param toolchain_ The inserted toolchain
   */
  public void insertToolchain(String id_, Toolchain toolchain_) {
    toolchains.put(id_, toolchain_);
  }
  
  @Override
  public void constructResolvers(ResolverLayer layer_) throws SMakeException {
    for(var toolchain_ : toolchains.values()) {
      toolchain_.constructResolvers(layer_);
    }
  }
}
