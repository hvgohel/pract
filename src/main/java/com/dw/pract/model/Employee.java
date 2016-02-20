package com.dw.pract.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ektorp.support.CouchDbDocument;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Employee extends CouchDbDocument {

  private String name;

  private String city;

  private List<Attachment> listAttachment;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public List<Attachment> getListAttachment() {
    return listAttachment;
  }

  public void setListAttachment(List<Attachment> listAttachment) {
    this.listAttachment = listAttachment;
  }

  public Map<String, Object> getEmployeeMap(Employee employee) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("name", employee.getName());
    map.put("city", employee.getCity());

    return map;
  }
}
