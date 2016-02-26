package com.dw.pract.repository;

import java.util.List;

import javax.inject.Inject;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.springframework.stereotype.Service;

import com.dw.pract.model.EmpAddress;

@Service
public class EmpAddressRepository extends CouchDbRepositorySupport<EmpAddress> {

  @Inject
  protected EmpAddressRepository(CouchDbConnector couchDbConnector) {
    super(EmpAddress.class, couchDbConnector);
    initStandardDesignDocument();
  }

  @GenerateView
  public List<EmpAddress> findByCity(String city) {
    return queryView("by_city", city);
  }

  @View(name = "count", map = "function(doc){if(doc.type == 'address'){ emit(null,1)}}", reduce = "_count")
  public int count() {
    ViewResult r = db.queryView(createQuery("count"));
    return r.getRows().get(0).getValueAsInt();
  }
}
