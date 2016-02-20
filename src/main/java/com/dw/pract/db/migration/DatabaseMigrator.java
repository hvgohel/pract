package com.dw.pract.db.migration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dw.pract.utils.DataSourceProvider;

/**
 * Application Lifecycle Listener implementation class DatabaseMigrator
 */
public class DatabaseMigrator implements ServletContextListener {

  private static Logger logger = LoggerFactory.getLogger(DatabaseMigrator.class);

  /**
   * @see ServletContextListener#contextInitialized(ServletContextEvent)
   */
  public void contextInitialized(ServletContextEvent sce) {
    try {
      logger.debug("Going to migrate database....");

      Flyway flyway = new Flyway();
      flyway.setDataSource(DataSourceProvider.only().getDataSource());
      flyway.setInitOnMigrate(true);
      flyway.setOutOfOrder(true);
      flyway.repair();

      int noOfMigrations = flyway.migrate();
      if (noOfMigrations > 0) {
        logger.info("{} migrations applied successfully.", noOfMigrations);
      }
    } catch (Exception e) {
      logger.error("DB migration failed.", e);
    }
  }

  /**
   * @see ServletContextListener#contextDestroyed(ServletContextEvent)
   */
  public void contextDestroyed(ServletContextEvent sce) {}
}
