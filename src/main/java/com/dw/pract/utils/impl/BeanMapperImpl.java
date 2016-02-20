package com.dw.pract.utils.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.dw.pract.utils.BeanMapper;

@Named("myBeanMapper")
public class BeanMapperImpl extends DozerBeanMapper implements BeanMapper {

  private static Logger logger = LoggerFactory.getLogger(BeanMapperImpl.class);

  @Inject
  public BeanMapperImpl(@Value("classpath*:dozer/*-dozer.xml") Resource[] resources) {
    super();

    try {
      List<String> mappings = new ArrayList<String>(resources.length);

      for (Resource r : resources) {
        String url = r.getURL().toString();
        logger.debug("Scanned Dozer mapping file : {}", url);
        mappings.add(url);
      }

      setMappingFiles(mappings);
    } catch (Exception e) {
      logger.error("Error occurred while scanning mapping files.");
    }
  }

  public <T, D> List<D> mapCollection(Collection<T> list, Class<D> classDTOName) {
    List<D> dtoList = new ArrayList<D>();

    if (list != null) {
      for (T f : list) {
        dtoList.add(map(f, classDTOName));
      }
    }

    return dtoList;
  }

  public <T, D> List<D> mapCollection(Collection<T> list, Class<D> classDTOName, String mapId) {
    List<D> dtoList = new ArrayList<D>();

    if (list != null) {
      for (T f : list) {
        dtoList.add(map(f, classDTOName, mapId));
      }
    }

    return dtoList;
  }
}
