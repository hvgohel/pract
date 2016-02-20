package com.dw.pract.utils;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

@Named
public class AppConfig {

  @Value("${couchdb.url}")
  private String couchdbURL;

  @Value("${couchdb.database.name}")
  private String couchdbDatabaseName;

  @Value("${couchdb.userName}")
  private String couchdbUserName;

  @Value("${couchdb.paasword}")
  private String couchdbPassword;

  public String getCouchdbURL() {
    return couchdbURL;
  }

  public String getCouchdbDatabaseName() {
    return couchdbDatabaseName;
  }

  public String getCouchdbPassword() {
    return couchdbPassword;
  }

  public String getCouchdbUserName() {
    return couchdbUserName;
  }
}
