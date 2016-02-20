package com.dw.pract.utils;

import java.io.InputStream;
import java.util.Properties;

import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * This class is used to get javax.sql.DataSource using spring context.
 */
@Named
public class DataSourceProvider {

  private static Logger logger = LoggerFactory.getLogger(DataSourceProvider.class);

  private static final String PROJECT_PROPERTIES = "project.properties";
  private static final String PROPERTY_URL = "db.url";
  private static final String PROPERTY_USERNAME = "db.user";
  private static final String PROPERTY_PASSWORD = "db.password";

  private static DataSourceProvider mOnly = null;

  public static DataSourceProvider only() {
    if (mOnly == null) {
      mOnly = new DataSourceProvider();
    }
    return mOnly;
  }

  public DataSourceProvider() {
    logger.debug("DataSourceProvider initialized");
    mOnly = this;
  }

  /**
   * Returns a Datasource which could be used to connect to the database which is to be migrated.
   * 
   * @return
   * @throws Exception
   */
  public DataSource getDataSource() throws Exception {
    Properties p = new Properties();

    InputStream is = getClass().getClassLoader().getResourceAsStream(PROJECT_PROPERTIES);

    if (is == null) {
      throw new RuntimeException(String.format("%s not found", PROJECT_PROPERTIES));
    }

    p.load(is);

    String url = p.getProperty(PROPERTY_URL);
    String username = p.getProperty(PROPERTY_USERNAME);
    String password = p.getProperty(PROPERTY_PASSWORD);

    if (StringUtils.isBlank(url) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      throw new RuntimeException(String.format("Either of these properties not found : %s, %s or %s", PROPERTY_URL,
          PROPERTY_USERNAME, PROPERTY_PASSWORD));
    }

    // url will contain '&amp;' as it comes from configuration but we need to interpret it as '&'
    // only so
    // replacing '&amp;' with '&'. Here it is not the proper way to do this but we don't found any
    // effective way to
    // decode it like this so we are doing this in this way at this time anyway
    url = url.replace("&amp;", "&");

    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setUser(username);
    dataSource.setPassword(password);
    dataSource.setUrl(url);

    return dataSource;
  }
}
