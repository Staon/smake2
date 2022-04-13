/*
 * Copyright (C) 2022 Ondrej Starek (Staon)
 *
 * This file is part of SMake2.
 *
 * SMake2 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OTest2 is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SMake2.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.staon.smake.core.dependencies;

import java.util.Objects;

/**
 * Identifier of a dependency node
 */
public final class ID {
  final private String id;
  
  /**
   * Ctor
   *
   * @param id_ String id
   */
  public ID(String id_) {
    id = id_;
  }
  
  @Override
  public String toString() {
    return '\'' + id + '\'';
  }
  
  @Override
  public boolean equals(Object o) {
    if(this == o)
      return true;
    if(o == null || getClass() != o.getClass())
      return false;
    ID id1 = (ID) o;
    return id.equals(id1.id);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
