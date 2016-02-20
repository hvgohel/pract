package com.dw.pract.model;

/**
 * This interface is used to provide common method to each entity.
 * 
 * Each entity and its DAO must implement this interface.
 * 
 * @author ashvin
 */
public interface Entity {

  /**
   * This method is used to check whether an id of an entity is valid or not.
   * 
   * @return true if id of an entity is not a whitespace, blank or null. Otherwise, false.
   */
  public boolean hasValidId();

}
