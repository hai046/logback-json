package com.hai046.logback.layout;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.MessageConverter;
import com.hai046.logback.converter.JsonLoggerConverter;

/**
 * @author haizhu
 * date 2019-05-21
 */
public class JsonPatternLayout extends PatternLayout {


    public JsonPatternLayout() {
        super();
        //转成json
        defaultConverterMap.put("m", JsonLoggerConverter.class.getName());
        defaultConverterMap.put("message", JsonLoggerConverter.class.getName());
        defaultConverterMap.put("msg", JsonLoggerConverter.class.getName());
        defaultConverterMap.put("jsonMsg", JsonLoggerConverter.class.getName());
        //保留原始格式输出
        defaultConverterMap.put("keepMsg", MessageConverter.class.getName());

        /**
         * 因为自己写的logutil 导致定位错乱，修正
         */
        defaultConverterMap.put("location", JsonLoggerConverter.class.getName());

    }
}
