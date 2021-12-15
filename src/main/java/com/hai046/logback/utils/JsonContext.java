package com.hai046.logback.utils;

/**
 * @author haizhu
 * date 2019-05-22
 */
public class JsonContext {


    static final ThreadLocal<Boolean> mThreadLocal = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
}
