package com.dw.pract.repository;

import java.util.List;

import javax.inject.Inject;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.springframework.stereotype.Service;

import com.dw.pract.model.Employee;

@Service
public class EmployeeRepository extends CouchDbRepositorySupport<Employee> {

  @Inject
  protected EmployeeRepository(CouchDbConnector db) {
    super(Employee.class, db);
    initStandardDesignDocument();
  }

  @GenerateView
  public List<Employee> findByName(String name) {
    return queryView("by_name", name);
  }

  @View(name = "count", map = "function(doc){if(doc.type == 'employee'){ emit(null,1)}}", reduce = "_count")
  public int count() {
    ViewResult r = db.queryView(createQuery("count"));
    return r.getRows().get(0).getValueAsInt();
  }

  public List<Employee> byName(String name) {
    return null;
  }
}
