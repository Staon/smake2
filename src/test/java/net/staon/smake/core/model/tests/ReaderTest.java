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

import net.staon.smake.core.model.dsl.ModelReader;
import net.staon.smake.core.model.dsl.ModelReaderException;
import net.staon.smake.core.model.dsl.ParseErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.google.common.truth.Truth.assertThat;

public class ReaderTest {
  @Test
  public void readerTest() throws ModelReaderException {
    var reader_ = new ModelReader();
    var project = reader_.readProject(
        """
        project("Hello") {
          block {
            artefact("hello", "bin") {
              sources(
                "hello.cpp"
              )
            }
          }
        }
        """,
        "test");
    assertNotNull(project);
    
    assertThrows(ParseErrorException.class, () -> {
      reader_.readProject(
          """
          project("ProjectA") {
          }
          
          project("ProjectB") {
          }
          """,
          "test");
    });
  }
}
