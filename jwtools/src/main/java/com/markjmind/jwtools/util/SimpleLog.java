package com.markjmind.jwtools.util;

import android.util.Log;

import com.markjmind.jwtools.log.Loger;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-05-18
 */
public class SimpleLog {
    private Class<?> tagClass;
    private String tag;
    public static boolean isLog = true;

    public SimpleLog(Class<?> tagClass){
        this.tagClass = tagClass;
        this.tag = tagClass.getSimpleName();
    }

    public SimpleLog(Class<?> tagClass, String tag){
        this.tagClass = tagClass;
        this.tag = tag;
    }

    public void e(String msg){
        if(SimpleLog.isLog) {
            Log.e(getLogPoint(), msg);
        }
    }

    public void i(String msg){
        if(SimpleLog.isLog) {
            Log.i(getLogPoint(), msg);
        }
    }

    public void d(String msg){
        if(SimpleLog.isLog) {
            Log.d(getLogPoint(), msg);
        }
    }

    public void w(String msg){
        if(SimpleLog.isLog) {
            Log.w(getLogPoint(), msg);
        }
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    private String getLogPoint(){
        return "log."+tag+Loger.callClass(tagClass);
    }

    public static void setLog(boolean isLog){
        SimpleLog.isLog = isLog;
    }

}
