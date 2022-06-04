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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * Builder of one smake project
 */
public class ProjectBuilder {
  private final Project project;
  private final Deque<ProjectPartContainer> blocks;
  private Artefact artefact;
  private final Set<String> artefacts;
  private Set<Path> sources;
  
  /**
   * Ctor
   *
   * @param name_ Name of the project
   */
  public ProjectBuilder(String name_) {
    project = new Project(name_);
    blocks = new ArrayDeque<>();
    blocks.push(project);
    artefact = null;
    artefacts = new HashSet<>();
    sources = null;
  }
  
  /**
   * Get the constructed project object
   */
  public Project getProject() {
    return project;
  }
  
  /**
   * Open new block inside a project (a new resolver layer)
   */
  public void openProjectBlock() {
    assert artefact == null;
    blocks.push(new ProjectBlock());
  }
  
  /**
   * Open definition of an artefact
   *
   * @param name_ Name of the artefact
   * @param type_ Type of the artefact
   */
  public void openArtefact(String name_, String type_)
      throws DuplicatedArtefactException {
    assert artefact == null;
  
    var artefact_ = new Artefact(name_, type_);
    if(artefacts.contains(name_)) {
      throw new DuplicatedArtefactException(project, artefact_);
    }
    artefacts.add(name_);
    artefact = artefact_;
    sources = new HashSet<>();
  }
  
  /**
   * Add new artefact's source
   */
  public void addSource(Path path_) throws DuplicatedSourceException {
    assert artefact != null && sources != null;

    /* -- check duplicated sources in the artefact */
    if(sources.contains(path_)) {
      throw new DuplicatedSourceException(project, artefact, path_);
    }
    
    artefact.addSource(new Source(path_));
    sources.add(path_);
  }
  
  /**
   * Close definition of an already opened artefact
   */
  public void closeArtefact() {
    assert artefact != null;
    assert !blocks.isEmpty();
    
    blocks.peek().addChild(artefact);
    artefact = null;
  }
  
  /**
   * Close active project block
   */
  public void closeProjectBlock() {
    assert blocks.peek() != null;
    var block_ = (ProjectBlock) blocks.pop();
    assert blocks.peek() != null;
    blocks.peek().addChild(block_);
  }
}
