## 修改log格式
```
logPattern={"project":"${loggerAppName}","timestamp":"%d{yyyy-MM-dd'T'HH:mm:ss.SSSZ}","log_level":"%level","traceId":"%X{X-B3-TraceId}","spanId":"%X{X-B3-SpanId}","thread":"%thread",%message}
```
## 修改logback
重点
```
<encoder class="JsonPatternLayoutEncoder">
```
例如

```xml

<appender name="STDERR-APPENDER" class="ch.qos.logback.core.ConsoleAppender">
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
        <level>ERROR</level>
    </filter>
    <encoder class="com.hai046.logback.layout.JsonPatternLayoutEncoder">
        <pattern>${logPattern}</pattern>
        <charset>utf8</charset>
    </encoder>
</appender>

<appender name="ERROR-APPENDER" class="ch.qos.logback.core.rolling.RollingFileAppender">
<file>${loggerRoot}/${loggerAppName}/error.log</file>
<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
    <fileNamePattern>${loggerRoot}/${loggerAppName}/error.log.%d{yyyy-MM-dd}.gz</fileNamePattern>
    <maxHistory>7</maxHistory>
</rollingPolicy>
<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
    <level>ERROR</level>
</filter>
<encoder class="com.hai046.logback.layout.JsonPatternLayoutEncoder">
    <pattern>${logPattern}</pattern>
    <charset>utf8</charset>
</encoder>
</appender>
```

依赖
```xml

            <dependency>
                <groupId>com.hai046.logback</groupId>
                <artifactId>logback-json</artifactId>
                <version>1.0-SNAPSHOT</version>
            </dependency>

```
