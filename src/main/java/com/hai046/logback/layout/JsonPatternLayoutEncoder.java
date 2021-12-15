package com.hai046.logback.layout;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

/**
 * @author haizhu
 * date 2019-05-21
 */
public class JsonPatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {

    @Override
    public void start() {
        JsonPatternLayout patternLayout = new JsonPatternLayout();
        patternLayout.setContext(context);

        String pattern = getPattern();

        if (pattern == null) {
            patternLayout.setPattern("{\"project\":\"${loggerAppName}\",\"timestamp\":\"%d{yyyy-MM-dd'T'HH:mm:ss.SSSZ}\",\"log_level\":\"%level\",\"traceId\":\"%X{X-B3-TraceId}\",\"spanId\":\"%X{X-B3-SpanId}\",\"thread\":\"%thread\",%message}\n");
        } else {
            if (!pattern.endsWith("\n")) {
                pattern += "\n";
            }
            patternLayout.setPattern(pattern);
        }
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }
}
