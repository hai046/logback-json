package com.hai046.logback;

import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * @author haizhu
 * date 2019-05-21
 */
public class JsonLoggerUtil {

    public static JsonBuilder info(Logger logger) {
        return JsonBuilder.Info(logger);
    }

    public static JsonBuilder warn(Logger logger) {
        return JsonBuilder.create(logger, Level.WARN);
    }

    public static JsonBuilder debug(Logger logger) {
        return JsonBuilder.create(logger, Level.DEBUG);
    }

    public static JsonBuilder error(Logger logger) {
        return JsonBuilder.create(logger, Level.ERROR);
    }

    public static JsonBuilder trace(Logger logger) {
        return JsonBuilder.create(logger, Level.TRACE);
    }


}
