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
package net.staon.smake.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generic path of a resource
 *
 * This class implements a generic resource path. Each smake resource
 * is identified by a resource type and a unique path in the type namespace.
 *
 * Smake paths are relative to the project, they are unique inside a project,
 * and they are case-sensitive. Generally, they don't represent filesystem
 * paths, but mostly they can be simply mapped to.
 */
public final class Path {
  private final List<String> path;
  
  /**
   * Ctor - empty path
   */
  public Path() {
    path = null;
  }
  
  /**
   * Ctor
   *
   * @param path_ Initial path value
   */
  private Path(List<String> path_) {
    assert !path_.isEmpty();
    path = path_;
  }
  
  /**
   * Ctor
   *
   * @param path_ Initial path value
   */
  public Path(Path path_) {
    path = path_.path;
  }
  
  /**
   * Ctor
   *
   * The method parses a path string. The string is a sequence of non-empty
   * path names separated by slashes. As all paths are relative the string
   * must not start by the slash. Empty string means empty path.
   *
   * @param path_ Initial path value represented as a string.
   */
  public Path(String path_) throws InvalidPathException {
    if(!path_.isEmpty()) {
      var list = path_.split(
        "/", -1 /* -- don't ignore trailing empty strings */);
      path = new ArrayList<>();
      for(var segment : list) {
        if(segment.isEmpty())
          throw new InvalidPathException(path_);
        path.add(segment);
      }
    }
    else {
      path = null;
    }
  }
  
  /**
   * Check whether the path is empty
   */
  public boolean isEmpty() {
    return path == null;
  }
  
  /**
   * Get path basename (last item in the path)
   */
  public String getBasename() {
    assert path != null && !path.isEmpty();
    return path.get(path.size() - 1);
  }
  
  /**
   * Parse extension (everything after last dot character) from the basename
   *
   * Note: the extension is just a naming convention. Smake uses the extension
   *     for detection of resource type, but the extension is not a special
   *     part of the path.
   * @return The extension or empty string, if there is no extension part.
   */
  public String getExtension() {
    var basename_ = getBasename();
    var dot_index_ = basename_.lastIndexOf('.');
    if(dot_index_ < 0)
      return "";
    else
      return basename_.substring(dot_index_ + 1);
  }
  
  /**
   * Get path base directory (path without last segment)
   */
  public Path getBasedir() {
    assert path != null && !path.isEmpty();
    var basedir_ = path.subList(0, path.size() - 1);
    if(!basedir_.isEmpty()) {
      return new Path(basedir_);
    }
    else {
      return new Path();
    }
  }
  
  /**
   * Convert path to its string representation parsable by the constructor
   */
  public String asString() {
    if(path == null)
      return "";
    return String.join("/", path);
  }
  
  /**
   * Join two paths
   *
   * This method appends a path to this path and creates new path object.
   * In other words, this method creates new path which is the path_
   * relatively based on this path.
   */
  public Path join(Path path_) {
    if(path == null && path_.path == null)
      return new Path();

    var list = new ArrayList<String>();
    if(path != null)
      list.addAll(path);
    if(path_.path != null)
      list.addAll(path_.path);
    
    return new Path(list);
  }
  
  /**
   * Join two paths
   */
  public static Path join(Path p1_, Path p2_) {
    return p1_.join(p2_);
  }
  
  @Override
  public boolean equals(Object o_) {
    if(this == o_) return true;
    if(o_ == null || getClass() != o_.getClass()) return false;
    Path path_ = (Path) o_;
    return Objects.equals(path, path_.path);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(path);
  }
  
  @Override
  public String toString() {
    return asString();
  }
}
