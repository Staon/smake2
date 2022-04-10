package net.staon.smake.core.dependencies;

import java.util.Objects;

/**
 * Identifier of a dependency node
 */
public final class ID {
  final private String id;
  
  /**
   * Ctor
   *
   * @param id_ String id
   */
  public ID(String id_) {
    id = id_;
  }
  
  @Override
  public String toString() {
    return '\'' + id + '\'';
  }
  
  @Override
  public boolean equals(Object o) {
    if(this == o)
      return true;
    if(o == null || getClass() != o.getClass())
      return false;
    ID id1 = (ID) o;
    return id.equals(id1.id);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
