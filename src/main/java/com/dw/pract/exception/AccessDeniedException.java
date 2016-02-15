package com.dw.pract.exception;

public class AccessDeniedException extends RuntimeException implements ErrorInfo
{
    private String code;

    public AccessDeniedException()
    {
        super();
    }

    public AccessDeniedException(String msg)
    {
        super(msg);
    }

    public AccessDeniedException(String code, String msg)
    {
        super(msg);
        this.code = code;
    }

    @Override
    public String getCode()
    {
        return code;
    }

}
