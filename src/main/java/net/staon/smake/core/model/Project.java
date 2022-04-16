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
package net.staon.smake.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Representation of one smake project
 */
public class Project {
  private String name;
  private final Map<String, Artefact> artefacts;
  
  
  /**
   * Ctor
   *
   * @param name_ Name of the project
   */
  public Project(String name_) {
    name = name_;
    artefacts = new HashMap<>();
  }
  
  /**
   * Get project name
   */
  public String getName() {
    return name;
  }
  
  /**
   * Add new artefact
   *
   * @param artefact_ The constructed artefact. Name of the artefact must
   *                  be unique in the project's namespace. The artefact
   *                  must not exist yet!
   */
  public void addArtefact(Artefact artefact_) {
    assert !artefacts.containsKey(artefact_.getName());
    artefacts.put(artefact_.getName(), artefact_);
  }
  
  /**
   * Get list of project artefacts
   */
  public Iterable<Artefact> getArtefacts() {
    return artefacts.values();
  }
}
