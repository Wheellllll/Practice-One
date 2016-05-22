package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiaoShanhe on 2016/5/22.
 */
public class LogbackTest {

    private final static Logger logger = LoggerFactory.getLogger("name2"); //根据名字获取logger

    public static void main(String[] args) {
        logger.trace("logback {}", "TRACE (TRACE < DEBUG < INFO < WARN < ERROR )");
        logger.debug("logback {}", "DEBUG (TRACE < DEBUG < INFO < WARN < ERROR )");
        logger.info("logback {}", "INFO (TRACE < DEBUG < INFO < WARN < ERROR )");
        logger.warn("logback {}", "WARN (TRACE < DEBUG < INFO < WARN < ERROR )");
        logger.error("logback {}", "ERROR (TRACE < DEBUG < INFO < WARN < ERROR )");
    }


}
