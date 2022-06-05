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
package net.staon.smake.core.model.dsl;

import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import net.staon.smake.core.exception.ParseErrorException;
import net.staon.smake.core.exception.SMakeException;
import net.staon.smake.core.model.Project;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.Reader;
import java.io.StringReader;

/**
 * Groovy DSL model reader
 *
 * This class is responsible for reading smake DSL files. It creates a Groovy
 * environment and it runs the DSL script.
 */
public class ModelReader {
  private final GroovyShell shell;
  
  /**
   * Ctor
   */
  public ModelReader() {
    var compiler_config_ = new CompilerConfiguration();
    compiler_config_.setScriptBaseClass(DelegatingScript.class.getName());
    shell = new GroovyShell(compiler_config_);
  }
  
  /**
   * Read an smake project
   *
   * @param reader_ A reader representing the project's SMakefile
   * @param filename_ A name of the SMakefile used in error messages
   * @return Parsed smake project
   */
  public Project readProject(Reader reader_, String filename_)
      throws SMakeException {
    var context_ = new Context(this);
    try {
      var script_ = (DelegatingScript) shell.parse(reader_, filename_);
      script_.setDelegate(new RootDirectives(context_));
      script_.run();
    }
    catch(Throwable exc_) {
      var converted_ = new ParseErrorException(exc_.getMessage());
      converted_.initCause(exc_);
      throw converted_;
    }
    
    if(context_.project == null)
      throw new ParseErrorException("missing project specification");
    return context_.project.getProject();
  }
  
  /**
   * Read an smake project from a string
   *
   * @param content_ The SMakefile content
   * @param filename_ A name used in error messages
   * @return Parsed smake project
   */
  public Project readProject(String content_, String filename_)
      throws SMakeException {
    return readProject(new StringReader(content_), filename_);
  }
}
