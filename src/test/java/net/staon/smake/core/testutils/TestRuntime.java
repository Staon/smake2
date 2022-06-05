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
import net.staon.smake.core.toolchain.ToolchainGroup;

/**
 * A testing runtime
 *
 * This class construct entire smake environment.
 */
public class TestRuntime implements AutoCloseable {
  public final ToolchainGroup toolchain;
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
    /* -- construct toolchains according to configuration */
    toolchain = createDefaultConfiguration();
    
    /* -- construct initial resolver stack */
    resolver_stack = ResolverLayer.createConfigLayer(null);
    toolchain.constructResolvers(resolver_stack);

    /* -- create project assembler (for direct reader tests, usually
     *    a project is parsed by repository) */
    project_assembler = new ProjectAssembler(resolver_stack);
  }
  
  @Override
  public void close() throws Exception {

  }
  
  private ToolchainGroup createDefaultConfiguration() throws SMakeException {
    var toolchain_ = new ToolchainGroup();
    return toolchain_;
  }
}
