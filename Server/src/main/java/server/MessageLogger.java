package server;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;

public class MessageLogger {

    private String file;
    private Logger logger;

    public MessageLogger(String file) {
        this.file = file;
        initLogger();
    }

    public void saveMessage(String message) {
        logger.info(message);
    }

    private void initLogger() {
        LoggerContext context = new LoggerContext();
        logger = context.getLogger(MessageLogger.class);
        logger.setAdditive(false);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setImmediateFlush(true);
        encoder.setPattern("%msg%n");
        encoder.start();

        FileAppender fileAppender = new FileAppender();
        fileAppender.setContext(context);
        fileAppender.setEncoder(encoder);
        fileAppender.setFile(file);
        fileAppender.start();

        logger.addAppender(fileAppender);
    }
}
