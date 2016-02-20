package com.dw.pract.exception;

/**
 * This class is used to throw resource not found exception with custom code.
 * 
 * @author ashvin
 */
public class ResourceNotFoundException extends RuntimeException implements ErrorInfo {

  String code;

  public ResourceNotFoundException() {
    super();
  }

  public ResourceNotFoundException(String msg) {
    super(msg);
  }

  public ResourceNotFoundException(String code, String msg) {
    this(msg);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }
}
