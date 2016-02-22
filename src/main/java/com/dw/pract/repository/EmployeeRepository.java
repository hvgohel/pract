package com.dw.pract.repository;

import javax.inject.Inject;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.springframework.stereotype.Service;

import com.dw.pract.model.Employee;

@Service
@View(name = "all", map = "function(doc) { if (doc.type == 'employee') { emit(null, doc) } }")
public class EmployeeRepository extends CouchDbRepositorySupport<Employee> {

  @Inject
  protected EmployeeRepository(CouchDbConnector db) {
    super(Employee.class, db);
    initStandardDesignDocument();
  }
}
