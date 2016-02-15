package com.dw.pract.dao;


import java.util.List;
import com.dw.pract.model.Entity;

/**
 * Each DAO will be based on an parameterized, abstract DAO class with support for the common generic database
 * operations.
 * 
 * @author ashvin
 * 
 * @param <T>
 *            represents entity type
 * @param <K>
 *            represents type of a primary key of an entity
 */
public interface AbstractDao<T extends Entity, K> {

    /**
     * This method is used to save the given entity.
     * 
     * @param entity
     *            received entity model object to save.
     */
    public abstract void save(T entity);

    /**
     * This method is used to delete the entity for given entity id.
     * 
     * @param entityId
     *            K representing unique entity id.
     */
    public abstract void delete(K entityId);

    /**
     * This method is used to delete the given entity.
     * 
     * @param entity
     *            received entity model object to delete.
     */
    public abstract void delete(T entity);

    /**
     * This method is used to fetch the entity for given entity id.
     * 
     * @param entityId
     *            K representing unique entity id.
     * @return T represents entity for given entity id.
     */
    public abstract T get(K entityId);

    /**
     * This method is used to fetch the list of entities.
     * 
     * @return List<T> list of entities.
     */
    public abstract List<T> list();

    /**
     * Flushes all the pending changes to the DB
     */
    public void flush();

}
