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
package net.staon.smake.core.execution;

import net.staon.smake.core.dependencies.ID;
import net.staon.smake.core.model.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic smake resource
 *
 * A resource is an abstraction representing mostly source and generated
 * files. However, they can represent just a virtual resources, e.g.
 * exported and imported files (they are just project interface, physical
 * file is represented by another resource).
 *
 * A resource is uniquely identified by the pair (type, path). That's it,
 * there can be resources with the same path but different type. E.g.
 * exported and physical resources can have the same.
 */
public abstract class Resource {
  private List<ProductReference> products;
  
  /**
   * Ctor
   */
  public Resource() {
    products = new ArrayList<>();
  }
  
  /**
   * Return type of the resource
   */
  public abstract String getType();
  
  /**
   * Return path of the resource
   */
  public abstract Path getPath();
  
  /**
   * Return unique ID of the resource
   */
  public final ResourceID getID() {
    return new ResourceID(getType(), getPath());
  }
  
  /**
   * Return graph ID of the resource
   */
  public final ID getGraphID() {
    return getID().asGraphID();
  }
  
  /**
   * Attach the resource to a product resource
   */
  public void attachWithProduct(ProductReference ref_) {
    products.add(ref_);
  }
  
  /**
   * Attach this resource with the same product resources as the @a ref_
   */
  public void attachWithSameProduct(Resource ref_) {
    products = ref_.products;
  }
}
