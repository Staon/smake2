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

import java.util.Objects;

/**
 * A reference object to a stage
 */
public class StageReference {
  private final String project;
  private final String stage;
  
  /**
   * Ctor
   *
   * @param project_ Name of the project
   * @param stage_ Name of the stage
   */
  StageReference(String project_, String stage_) {
    project = project_;
    stage = stage_;
  }
  
  @Override
  public boolean equals(Object o_) {
    if(this == o_) return true;
    if(o_ == null || getClass() != o_.getClass()) return false;
    StageReference that = (StageReference) o_;
    return project.equals(that.project) && stage.equals(that.stage);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(project, stage);
  }
}
