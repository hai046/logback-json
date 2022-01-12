package com.hai046.logback.converter;


import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.util.CachingDateFormatter;

import java.util.List;
import java.util.TimeZone;

/**
 * @author hai046
 * @date 2021/12/15
 **/
public class ESTimestampConverter extends ClassicConverter {

    CachingDateFormatter cachingDateFormatter = null;

    @Override
    public void start() {

        String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        try {
            cachingDateFormatter = new CachingDateFormatter(datePattern);
        } catch (IllegalArgumentException e) {
            addWarn("Could not instantiate SimpleDateFormat with pattern " + datePattern, e);
            // default to the ISO8601 format
            cachingDateFormatter = new CachingDateFormatter(CoreConstants.ISO8601_PATTERN);
        }
        List<String> optionList = getOptionList();
        // if the option list contains a TZ option, then set it.
        if (optionList != null && optionList.size() > 1) {
            TimeZone tz = TimeZone.getTimeZone((String) optionList.get(1));
            cachingDateFormatter.setTimeZone(tz);
        }
    }

    @Override
    public String convert(ILoggingEvent le) {
        long timestamp = le.getTimeStamp();
        return cachingDateFormatter.format(timestamp);
    }
}
