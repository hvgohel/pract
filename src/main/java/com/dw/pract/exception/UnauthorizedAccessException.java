package com.dw.pract.exception;

public class UnauthorizedAccessException extends RuntimeException implements ErrorInfo {
  private String code;

  public UnauthorizedAccessException() {
    super();
  }

  public UnauthorizedAccessException(String msg) {
    super(msg);
  }

  public UnauthorizedAccessException(String code, String msg) {
    super(msg);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }

}
