package com.hai046.logback.splitword;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单符号分析
 *
 * @author lanzhaoyi
 * @create 2019-05-23 16:20
 **/
public class SimpleSymbolAnalysis {

    /**
     * 拆解标志
     */
    private static final String[] DISMANTLING_FLAG = new String[]{
            "{}"
    };
    /**
     * 移除标志
     */
    private static final String[] REMOVE_FLAG = new String[]{
            ":", "=", ","
    };


    /**
     * 将消息字符串转通用json
     *
     * @param dataMap    数据字典
     * @param message    内容
     * @param sourceData 真实数据值
     * @return 是否匹配解析
     */
    public static boolean handle(Map<String, Object> dataMap, String message, Object... sourceData) {
        if (dataMap == null) {
            return false;
        }
        if (StringUtils.isEmpty(message)) {
            return false;
        }
        if (sourceData == null || sourceData.length <= 0) {
            return false;
        }
        StringBuilder stringBuilder = null;
        for (String dFlag : DISMANTLING_FLAG) {
            String[] dataSplit = StringUtils.split(message, dFlag);
            if (dataSplit == null) {
                continue;
            }
            stringBuilder = new StringBuilder();
            for (int i = 0; i < dataSplit.length; i++) {
                String key = dataSplit[i].trim();
                if (i >= sourceData.length) {
                    stringBuilder.append(key);
                    continue;
                }
                stringBuilder.append(key).append(sourceData[i]);
                for (String rflag : REMOVE_FLAG) {
                    if (key.endsWith(rflag)) {
                        key = key.substring(0, key.length() - 1);
                    }
                    if (key.startsWith(rflag)) {
                        key = key.substring(1);
                    }
                }
                key = key.trim();
                int startIndex = 0;
                if ((startIndex = key.lastIndexOf(" ")) >= 0) {
                    key = key.substring(startIndex);
                }
                if (StringUtils.isNotEmpty(key)) {
                    dataMap.put(key.trim(), sourceData[i]);
                }
            }
            break;
        }

        return true;
    }

    public static void main(String[] args) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Map<String, Object> dataMap = new HashMap<>();
        SimpleSymbolAnalysis.handle(dataMap, "JdkpAccountService getBanlance response result:{} name:{} ,age:{}", "aaa", "zhangsan", 18);

        dataMap = new HashMap<>();
        SimpleSymbolAnalysis.handle(dataMap, "filter-> checkNeedConfig error, config = {}", "ffffff");

        dataMap = new HashMap<>();
        SimpleSymbolAnalysis.handle(dataMap, "pubObject error obj {} to map {}", "1", 2, 3, 4);

        stopWatch.stop();
        System.out.println(String.format("time=%s,nanotime=%s", stopWatch.getTime(), stopWatch.getNanoTime()));
    }
}
