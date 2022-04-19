import com.hai046.logback.JsonLoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author haizhu
 * date 2019-05-21
 */
public class Test {
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String args[]) {
        JsonLoggerUtil.info(logger).orderNo("T2012121123121")
                .put("agent", 11212)
                .put("1212", 12).msg("11", "你好", 12)
                .build();
        logger.info("\n\n1111111");
        System.exit(0);
    }

}
