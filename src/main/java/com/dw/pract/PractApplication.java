package com.dw.pract;

import javax.inject.Inject;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dw.pract.utils.AppConfig;

@SpringBootApplication
public class PractApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(PractApplication.class);

  @Inject
  private AppConfig appConfig;

  public static void main(String[] args) {
    LOGGER.debug("Welcome to application start");
    SpringApplication.run(PractApplication.class, args);
  }

  @Bean(name = "couchDbConnector")
  public CouchDbConnector getCouchDB() {
    
    HttpClient httpClient = null;
    
    try {
      httpClient = new StdHttpClient.Builder()
          .url(appConfig.getCouchdbURL())
          .username(appConfig.getCouchdbUserName())
          .password(appConfig.getCouchdbPassword())
          .build();

      CouchDbInstance couchDbInstance = new StdCouchDbInstance(httpClient);
      CouchDbConnector couchDbConnector = couchDbInstance.createConnector(appConfig.getCouchdbDatabaseName(), true);
      return couchDbConnector;
    } catch (Exception e) {
      throw new RuntimeException("Error while connecting couchdb : ", e);
    }
  }
}
