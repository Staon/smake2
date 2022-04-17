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

import net.staon.smake.core.model.Artefact;

/**
 * A reference to a product resource
 *
 * Resources keep this reference to know which product they belong to.
 */
public class ProductReference {
  private final Artefact artefact;
  private final String type;
  private final Resource product;
  
  /**
   * Ctor
   *
   * @param artefact_ Product artefact
   * @param type_ Product type
   * @param product_ Product resource
   */
  public ProductReference(Artefact artefact_, String type_, Resource product_) {
    artefact = artefact_;
    type = type_;
    product = product_;
  }
  
  /**
   * Get the artefact
   */
  public Artefact getArtefact() {
    return artefact;
  }
  
  /**
   * Get product type
   */
  public String getType() {
    return type;
  }
  
  /**
   * Get product resource
   */
  public Resource getResource() {
    return product;
  }
}
