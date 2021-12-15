package com.hai046.logback.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author haizhu
 * date 2019-05-22
 */
public class JsonObjectMapperUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
