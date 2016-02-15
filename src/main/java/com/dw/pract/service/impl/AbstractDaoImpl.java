package com.dw.pract.service.impl;


import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.dw.pract.dao.AbstractDao;
import com.dw.pract.dao.OrdersProvider;
import com.dw.pract.dao.PagedResultHelper;
import com.dw.pract.dao.PredicateProvider;
import com.dw.pract.model.Entity;
import com.dw.pract.model.paging.PagedResult;

/**
 * An implementation for AbstractDao.
 * 
 * @author developer
 * 
 * @param <T>
 *            represents entity type
 * @param <K>
 *            represents type of a primary key of an entity
 */
public abstract class AbstractDaoImpl<T extends Entity, K> implements AbstractDao<T, K> {

    @SuppressWarnings("unchecked")
    protected Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * (non-Javadoc)
     * 
     * @see com.dw.baseproject.dao.AbstractDao#save(T)
     */
    public void save(final T entity) {
        if (!entity.hasValidId()) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.dw.baseproject.dao.AbstractDao#delete(K)
     */
    public void delete(final K entityId) {
        delete(get(entityId));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.dw.baseproject.dao.AbstractDao#delete(T)
     */
    public void delete(final T entity) {
        entityManager.remove(entity);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.dw.baseproject.dao.AbstractDao#get(K)
     */
    public T get(final K entityId) {
        return entityManager.find(clazz, entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.dw.baseproject.dao.AbstractDao#list()
     */
    public List<T> list() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        Root<T> criteriaRoot = criteria.from(clazz);
        criteria.select(criteriaRoot);

        return getEntityManager().createQuery(criteria).getResultList();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.dw.baseproject.dao.AbstractDao#flush()
     */
    public void flush() {
        entityManager.flush();
    }

    /**
     * This method is used to get the entity manager
     * 
     * @return EntityManager
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected PagedResult<T> createListResponse(final PagedResultHelper<T> helper, int rpp, int pageNo,
            final String sortOrder, final String sortOn) {

        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        OrdersProvider<T> ordersProvider = new OrdersProvider<T>() {

            public List<Order> getOrders(Root<T> root) {
                Path<?> path = helper.getPath(root);
                List<Order> orders = new ArrayList<Order>();

                // set Sorting criteria
                if (!StringUtils.isBlank(sortOn) && !StringUtils.isBlank(sortOrder)) {
                    if ("desc".equals(sortOrder)) {
                        orders.add(cb.desc(path.get(sortOn)));
                    } else {
                        orders.add(cb.asc(path.get(sortOn)));
                    }
                }
                return orders;
            }

        };

        PagedResult<T> result = createListResponse(helper, rpp, pageNo, ordersProvider);
        result.setSortOn(sortOn);
        result.setSortOrder(sortOrder);
        return result;

    }

    protected PagedResult<T> createListResponse(PredicateProvider<T> helper, int rpp, int pageNo,
            OrdersProvider<T> ordersProvider) {
        PagedResult<T> result = new PagedResult<T>(rpp, null, null);

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        // Find total records available.
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(clazz);
        countQuery.where(helper.getPredicate(countRoot));
        countQuery.select(cb.count(countRoot));
        Long totalRecords = getEntityManager().createQuery(countQuery).getSingleResult();
        result.setTotalResults(totalRecords);

        // If request page is beyond the available page, set it to the last page
        // available.
        int lastPage = (int) (totalRecords / rpp);
        if ((totalRecords % rpp) != 0) {
            lastPage += 1;
        }
        if (pageNo > lastPage) {
            pageNo = lastPage > 0 ? lastPage : 1;
        }
        result.setPageNo(pageNo);

        // Create list response criteria
        CriteriaQuery<T> listQuery = cb.createQuery(clazz);
        Root<T> listRoot = listQuery.from(clazz);
        listQuery.where(helper.getPredicate(listRoot));
        listQuery.orderBy(ordersProvider.getOrders(listRoot));

        // execute List Query and save result in PagedResult object.
        result.setResults(getEntityManager().createQuery(listQuery).setFirstResult(rpp * (pageNo - 1))
                .setMaxResults(rpp).getResultList());

        return result;
    }
}
