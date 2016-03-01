package com.dw.pract.model;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.ektorp.docref.CascadeType;
import org.ektorp.docref.DocumentReferences;
import org.ektorp.docref.FetchType;
import org.ektorp.support.CouchDbDocument;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Employee extends CouchDbDocument {

  @NotNull
  private String name;

  @NotNull
  private String type;

  @DocumentReferences(cascade = CascadeType.ALL, fetch = FetchType.LAZY, backReference = "empId")
  private Set<EmpAddress> addresses;

  private List<Attachment> listAttachment;

  private Result result;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Attachment> getListAttachment() {
    return listAttachment;
  }

  public void setListAttachment(List<Attachment> listAttachment) {
    this.listAttachment = listAttachment;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Set<EmpAddress> getAddresses() {
    return addresses;
  }

  public void setAddresses(Set<EmpAddress> addresses) {
    this.addresses = addresses;
  }

  public Result getResult() {
    return result;
  }

  public void setResult(Result result) {
    this.result = result;
  }

  public enum Result {
    PASS, FAIL
  }
}
