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
package net.staon.smake.core.model.tests;

import net.staon.smake.core.exception.SMakeException;
import net.staon.smake.core.model.dsl.ModelReader;
import net.staon.smake.core.exception.ModelReaderException;
import net.staon.smake.core.exception.ParseErrorException;
import net.staon.smake.core.testutils.TestRuntime;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Negative tests of the reader - happy paths are well tested in other
 * tests.
 */
public class ReaderTest {
  public TestRuntime runtime;
  
  @BeforeEach
  void startUp() throws SMakeException {
    runtime = new TestRuntime();
  }
  
  @AfterEach
  void tearDown() throws Exception {
    runtime.close();
  }
  
  @Test
  public void twoProjects() {
    assertThrows(ParseErrorException.class, () -> {
      runtime.project_assembler.assemblyProject(
          """
          project("ProjectA") {
          }
          
          project("ProjectB") {
          }
          """,
          "test");
    });
  }
  
  @Test
  public void missingProject() {
    assertThrows(ParseErrorException.class, () -> {
      runtime.project_assembler.assemblyProject(
          """
          artefact("hello", "bin") {
            sources(
              "hello.cpp"
            )
          }
          """,
          "test");
    });
  }
}
