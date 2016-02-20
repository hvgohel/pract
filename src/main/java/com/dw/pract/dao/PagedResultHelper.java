package com.dw.pract.dao;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dw.pract.model.Entity;

public interface PagedResultHelper<T extends Entity> extends PredicateProvider<T> {

  Predicate getPredicate(Root<T> root);

  Path<?> getPath(Root<T> root);
}
