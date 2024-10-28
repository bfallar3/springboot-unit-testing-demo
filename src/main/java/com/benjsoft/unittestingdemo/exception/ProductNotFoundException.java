package com.benjsoft.unittestingdemo.exception;

public class ProductNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String errorCode;
    private final transient Object[] parameters;

    public ProductNotFoundException(String message) {
        this(message, "DEFAULT_ERROR", null, null);
    }

    public ProductNotFoundException(String message, String errorCode) {
        this(message, errorCode, null, null);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        this(message, "DEFAULT_ERROR", cause, null);
    }

    public ProductNotFoundException(String message, String errorCode, Throwable cause, Object[] parameters) {
        super(message, cause);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object[] getParameters() {
        return parameters != null ? parameters.clone() : null;
    }
}
