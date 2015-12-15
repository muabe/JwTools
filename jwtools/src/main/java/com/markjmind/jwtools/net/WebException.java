package com.markjmind.jwtools.net;

/**
 * Created by codemasta on 2015-10-26.
 */
public class WebException extends Exception  {
    public String message;
    private Throwable exception;

    public WebException(String message, Throwable exception) {
        super(message, exception);
        this.message = message;
        this.exception = exception;
    }

    public WebException(String message) {
        super(message);
        this.message = message;
    }
}
