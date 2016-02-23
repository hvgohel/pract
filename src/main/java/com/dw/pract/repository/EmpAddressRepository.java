package com.dw.pract.repository;

import javax.inject.Inject;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.springframework.stereotype.Service;

import com.dw.pract.model.EmpAddress;

@Service
@View(name = "ektorp_docrefs_addresses", map = "function(doc) { if(doc.empId) { emit(null, doc); } }")
public class EmpAddressRepository extends CouchDbRepositorySupport<EmpAddress> {

  @Inject
  protected EmpAddressRepository(CouchDbConnector couchDbConnector) {
    super(EmpAddress.class, couchDbConnector);
    initStandardDesignDocument();
  }
}
