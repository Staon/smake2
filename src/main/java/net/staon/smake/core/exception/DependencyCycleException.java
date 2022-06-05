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
package net.staon.smake.core.exception;

/**
 * This exception is thrown by the the topological order calculator
 * when a cycle is detected.
 */
public class DependencyCycleException extends SMakeException {
  public DependencyCycleException() {
    super("Detected cycle in dependencies");
  }
}
