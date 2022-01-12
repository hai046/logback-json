package com.hai046.logback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hai046.logback.utils.JsonObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by haizhu on 2019-05-22
 * <p></p>
 *
 * @author haizhu12345@gmail.com
 * 打印json日志的封装类
 */
public class JsonBuilder {
    private final Level level;
    private final Logger logger;
    private final HashMap<String, Object> items = new HashMap<>(4);


    public JsonBuilder(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    public static JsonBuilder create(Logger logger, Level level) {
        return new JsonBuilder(logger, level);
    }

    public static JsonBuilder Info(Logger logger) {
        return create(logger, Level.INFO);
    }


    public JsonBuilder id(Number id) {
        return put("id", id.longValue());
    }

    public JsonBuilder msg(Object... msg) {
        final String result = toString(msg);
        if (result != null) {
            return put("msg", result);
        }
        return this;
    }

    /**
     * 腾讯的一个sdk 必须使用hashmap,哎
     * @return
     */
    public HashMap<String, Object> getMap(){
        return items;
    }

    /**
     * 订单号
     *
     * @param orderNo
     * @return
     */
    public JsonBuilder orderNo(String orderNo) {
        return put("orderNo", orderNo);
    }

    /**
     * 是否报警，如果设置了表示需要发送报警
     *
     * @return
     */
    public JsonBuilder alert() {
        return put("xl_alert", true);
    }

    public JsonBuilder sql(String sql) {
        return put("sql", sql);
    }

    public JsonBuilder costTime(long costTime) {
        return put("costTime", costTime);
    }

    public JsonBuilder exception(Throwable throwable) {
        return put(null, throwable);
    }

    /**
     * 打印日志，如果不是异常的话，必须有key；只有异常的时候默认会添加一个为exception 字段的key
     *
     * @param key
     * @param value
     * @return
     */
    public JsonBuilder put(String key, Object value) {
        if (value instanceof Throwable) {
            if (key == null) {
                key = "exception";
            }
            items.put(key, getStackTraceInfo((Throwable) value));
        } else {
            if (key == null) {
                logger.warn(message("日志必须先有可以在有value"));
                return this;
            }
            items.put(key, value);

        }
        return this;
    }

    /**
     * 把对象打印出
     *
     * @param obj
     * @return
     */
    public JsonBuilder pubObject(Object obj) {

        if (obj == null) {
            return this;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                if (getter != null && Modifier.isPublic(getter.getModifiers())) {
                    items.put(key, getter.invoke(obj));
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            logger.warn("pubObject error obj {} to map {}   errmsg={}", obj, obj.getClass(), e.getMessage());
        }


        return this;
    }


    private String getStackTraceInfo(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try {
            try (PrintWriter pw = new PrintWriter(sw, false)) {
                throwable.printStackTrace(pw);
                pw.flush();
                sw.flush();
            }
            return sw.toString();
        } finally {
            try {
                sw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String message(String s) {
        return String.format("\"message\":\"%s\"", s);
    }

    @Override
    public String toString() {
        try {
            return JsonObjectMapperUtils.getMapper().writeValueAsString(items);
        } catch (JsonProcessingException e) {
            return message(e.getMessage());
        }
    }

    /**
     * 打印完成必须该方法
     */
    public void build() {
        switch (level) {
            case INFO:
                logger.info(toString());
                break;
            case DEBUG:
                logger.debug(toString());
                break;
            case TRACE:
                logger.trace(toString());
                break;
            case ERROR:
                logger.error(toString());
                break;
            case WARN:
            default:
                logger.warn(toString());
        }
        items.clear();
    }

    public static String toString(Object[] a) {
        if (a == null) {
            return null;
        }

        int iMax = a.length - 1;
        if (iMax == -1) {
            return null;
        }

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax) {
                return b.toString();
            }
            b.append(", ");
        }
    }
}
