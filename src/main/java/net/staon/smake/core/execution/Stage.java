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

/**
 * One compilation stage
 *
 * Stage is a container of compilation tasks. Stage is a project's entrance
 * point - inter-project dependencies are computed between project stages
 * and the entire compilation order is defined by the order of stages.
 * In addition, at command line the root stages are specified in order to
 * tell smake what the user is intending to compile.
 */
public class Stage {
  private final String name;
  
  /**
   * Ctor
   *
   * @param name_ Name of the stage. The name must be unique inside project
   *              namespace.
   */
  public Stage(String name_) {
    name = name_;
  }
}
