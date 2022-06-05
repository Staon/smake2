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
package net.staon.smake.core.assembler;

import net.staon.smake.core.exception.SMakeException;
import net.staon.smake.core.execution.ResourceMap;
import net.staon.smake.core.model.dsl.ModelReader;
import net.staon.smake.core.resolver.ResolverContext;
import net.staon.smake.core.resolver.ResolverLayer;

import java.io.Reader;
import java.io.StringReader;

/**
 * Project assembler
 *
 * The assembler is responsible for constructing an smake project from its
 * SMakefile. Generally, it takes an SMakefile, parses it, and applies
 * resolvers to create the execution model (mainly resource map and
 * command builders).
 */
public class ProjectAssembler {
  private final ResolverLayer resolver_stack;
  private final ModelReader reader;
  
  /**
   * Ctor
   *
   * @param resolver_stack_ Configured resolver stack
   */
  public ProjectAssembler(ResolverLayer resolver_stack_) {
    resolver_stack = resolver_stack_;
    reader = new ModelReader();
  }
  
  /**
   * Assembly an smake project
   *
   * @param smakefile_ Reader accessing the project's SMakefile
   * @param filename_ Name of the file shown in error messages
   * @return The assembled project
   */
  public SMakeProject assemblyProject(Reader smakefile_, String filename_)
      throws SMakeException {
    /* -- parse the SMakefile */
    var project_model_ = reader.readProject(smakefile_, filename_);
    
    /* -- resolve the project */
    var resource_map_ = new ResourceMap();
    var resolver_context_ = new ResolverContext(resolver_stack, resource_map_);
    resolver_context_.resolveProject(project_model_);
    
    return new SMakeProject(resource_map_);
  }
  
  /**
   * Assembly an smake project
   *
   * @param smakefile_ Content of the project's SMakefile
   * @param filename_ Name of the file shown in error messages
   * @return The assembled project
   */
  public SMakeProject assemblyProject(String smakefile_, String filename_)
      throws SMakeException {
    return assemblyProject(new StringReader(smakefile_), filename_);
  }
}
