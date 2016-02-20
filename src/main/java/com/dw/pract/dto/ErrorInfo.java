package com.dw.pract.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This class is used to send the exception message in the response of any request (API call).
 * 
 * @author ashvin
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ErrorInfo {

  private String code;
  private String message;

  public ErrorInfo() {}

  public ErrorInfo(String message) {
    this.message = message;
  }

  public ErrorInfo(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
