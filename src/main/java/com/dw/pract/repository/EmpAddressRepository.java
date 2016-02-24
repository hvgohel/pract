package com.dw.pract.repository;

import javax.inject.Inject;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.springframework.stereotype.Service;

import com.dw.pract.model.EmpAddress;

@Service
public class EmpAddressRepository extends CouchDbRepositorySupport<EmpAddress> {

  @Inject
  protected EmpAddressRepository(CouchDbConnector couchDbConnector) {
    super(EmpAddress.class, couchDbConnector);
    initStandardDesignDocument();
  }
}
