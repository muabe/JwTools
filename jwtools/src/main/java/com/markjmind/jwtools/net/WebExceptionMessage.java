package com.markjmind.jwtools.net;

/**
 * Created by markj on 2015-12-04.
 */
public abstract class WebExceptionMessage {
    private WebExceptionMessage instance;

    public abstract String getExceptionMessage(Exception e);

    public abstract String getCodeMessage(Result result, int code);

}
