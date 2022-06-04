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

import net.staon.smake.core.execution.Resource;
import net.staon.smake.core.model.Path;

/**
 * Specification of product resources
 */
public interface ProductSpec {
  /**
   * Get the resource object
   */
  Resource getProductResource();
  
  /**
   * Get name of product type
   */
  String getProductType();
  
  /**
   * Mangle path of a resource which belongs to the product
   *
   * @param path_ Original resource path
   * @return New mangled resource path
   */
  Path mangleResourcePath(Path path_);
}
