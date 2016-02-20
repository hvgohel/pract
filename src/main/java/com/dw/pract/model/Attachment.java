package com.dw.pract.model;

import java.io.File;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Attachment {

  private String fileName;

  private String contentType;

  private File file;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }
}
