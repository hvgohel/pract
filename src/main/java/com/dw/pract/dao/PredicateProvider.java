package com.dw.pract.dao;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dw.pract.model.Entity;

public interface PredicateProvider<T extends Entity> {

  Predicate getPredicate(Root<T> root);
}
