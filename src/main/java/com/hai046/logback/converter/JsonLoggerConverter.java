package com.hai046.logback.converter;

import ch.qos.logback.classic.pattern.LoggerConverter;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hai046.logback.utils.JsonObjectMapperUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author haizhu
 * date 2019-05-21
 */
public class JsonLoggerConverter extends LoggerConverter {


    public static String getHostNameForLiunx() {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage();
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "UnknownHost";
        }
    }


    public static String getHostName() {
        if (System.getenv("COMPUTERNAME") != null) {
            return System.getenv("COMPUTERNAME");
        } else {
            return getHostNameForLiunx();
        }
    }

    private static final String HOST_NAME = getHostName();

    private ThrowableProxyConverter throwableHandlingConverter = new ThrowableProxyConverter();

    @Override
    public void start() {
        super.start();
        throwableHandlingConverter.start();
    }

    @Override
    public void stop() {
        super.stop();
        throwableHandlingConverter.stop();
    }

    @Override
    public String convert(ILoggingEvent le) {

        String s = "";

        //这里转换一下断 类名加加上行数
        String location = le.getLoggerName();
        if (location != null) {
            final String[] items = location.split("[.]");
            if (items.length > 1) {
                StringBuffer sb = new StringBuffer();
                int i = 0;
                for (; i < items.length - 1; i++) {
                    sb.append(items[i].charAt(0)).append(".");
                }
                sb.append(items[i]);
                location = sb.toString();
            }
        }

        String message = le.getFormattedMessage();
        //先不管数组了
        if (message.startsWith("{") && message.endsWith("}")) {
            location = location + ":" + getLineNumber(le, 1);
            final int length = message.length();
            if (length > 4) {
                //去掉json 前后括号
                message = message.substring(1, length - 1);
                s = String.format("\"location\":\"%s\",\"hostName\":\"%s\",%s", location, HOST_NAME, message);
            }
        } else {
            location = location + ":" + getLineNumber(le, 0);
        }

        try {
            s = String.format("\"location\":\"%s\",\"hostName\":\"%s\",\"msg\":%s", location, HOST_NAME, JsonObjectMapperUtils.getMapper().writeValueAsString(message));
        } catch (JsonProcessingException e) {
            s = String.format("\"location\":\"%s\",\"hostName\":\"%s\",\"msg\":\"%s\"", location, HOST_NAME, message);
        }

        String throwMessage = throwableHandlingConverter.convert(le);
        if (throwMessage != null && !throwMessage.isEmpty()) {
            try {
                s = s + String.format(",\"throwable\":%s", JsonObjectMapperUtils.getMapper().writeValueAsString(throwMessage));
            } catch (JsonProcessingException e) {
                s = s + String.format(",\"throwable\":\"%s\"", throwMessage);
            }

        }

        return s;

    }


    private int getLineNumber(ILoggingEvent le, int offsetStackTraceElement) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > offsetStackTraceElement) {
            return cda[offsetStackTraceElement].getLineNumber();
        }
        return -1;
    }


}
