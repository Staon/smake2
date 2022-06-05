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
package net.staon.smake.core.testutils;

import net.staon.smake.core.assembler.ProjectAssembler;
import net.staon.smake.core.exception.SMakeException;
import net.staon.smake.core.resolver.ResolverLayer;

/**
 * A testing runtime
 *
 * This class construct entire smake environment.
 */
public class TestRuntime implements AutoCloseable {
  public final ResolverLayer resolver_stack;
  public final ProjectAssembler project_assembler;
  
  /**
   * Create runtime with default configuration
   */
  public TestRuntime() throws SMakeException {
    this(new TestRuntimeConfig());
  }
  
  /**
   * Create runtime with specified configuration
   *
   * @param config_ The configuration object
   */
  public TestRuntime(TestRuntimeConfig config_) throws SMakeException {
    resolver_stack = createDefaultConfiguration();
    project_assembler = new ProjectAssembler(resolver_stack);
  }
  
  @Override
  public void close() throws Exception {

  }
  
  private ResolverLayer createDefaultConfiguration() throws SMakeException {
    var resolver_stack_ = ResolverLayer.createConfigLayer(null);
    return resolver_stack_;
  }
}
