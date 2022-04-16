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

import net.staon.smake.core.model.InvalidPathException;
import net.staon.smake.core.model.Path;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathTest {
  @Test
  public void parserTest() throws InvalidPathException {
    var empty_ = new Path("");
    assertTrue(empty_.isEmpty());
    assertEquals("", empty_.asString());
    
    var p1_ = new Path("main.cpp");
    assertFalse(p1_.isEmpty());
    assertEquals("main.cpp", p1_.asString());
    assertEquals("main.cpp", p1_.getBasename());
    assertEquals("cpp", p1_.getExtension());
    assertTrue(p1_.getBasedir().isEmpty());
    assertEquals("", p1_.getBasedir().asString());
    
    var p2_ = new Path("src/main.cpp");
    assertFalse(p2_.isEmpty());
    assertEquals("src/main.cpp", p2_.asString());
    assertEquals("main.cpp", p2_.getBasename());
    assertEquals("cpp", p2_.getExtension());
    assertFalse(p2_.getBasedir().isEmpty());
    assertEquals("src", p2_.getBasedir().asString());
    assertEquals("src", p2_.getBasedir().getBasename());
    assertEquals("", p2_.getBasedir().getExtension());
    assertTrue(p2_.getBasedir().getBasedir().isEmpty());
    assertEquals("", p2_.getBasedir().getBasedir().asString());
    
    assertThrowsExactly(InvalidPathException.class, ()->new Path("/src/main.cpp"));
    assertThrowsExactly(InvalidPathException.class, ()->new Path("src//main.cpp"));
    assertThrowsExactly(InvalidPathException.class, ()->new Path("src/main.cpp/"));
  }
  
  @Test
  public void joinTests() throws InvalidPathException {
    var empty_ = new Path();
    var p1_ = new Path("src/main.cpp");
    var p2_ = new Path("extension/submodule/weird-project");
    
    assertEquals(new Path("src/main.cpp"), empty_.join(p1_));
    assertEquals(new Path("src/main.cpp"), p1_.join(empty_));
    assertEquals(new Path("src/main.cpp"), Path.join(empty_, p1_));
    assertEquals(new Path("src/main.cpp"), Path.join(p1_, empty_));
  
    assertEquals(new Path("src/main.cpp/src/main.cpp"), p1_.join(p1_));
    assertEquals(new Path("src/main.cpp/src/main.cpp"), Path.join(p1_, p1_));
    assertEquals(new Path(), empty_.join(empty_));
    assertEquals(new Path(), Path.join(empty_, empty_));
  
    assertEquals(new Path("src/main.cpp/extension/submodule/weird-project"), p1_.join(p2_));
    assertEquals(new Path("extension/submodule/weird-project/src/main.cpp"), p2_.join(p1_));
    assertEquals(new Path("src/main.cpp/extension/submodule/weird-project"), Path.join(p1_, p2_));
    assertEquals(new Path("extension/submodule/weird-project/src/main.cpp"), Path.join(p2_, p1_));
  }
}
