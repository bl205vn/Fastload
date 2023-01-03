package io.github.bumblesoftware.fastload.util.log;

import io.github.bumblesoftware.fastload.init.Fastload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record SingleLineLogger(Logger logger) {
    public static SingleLineLogger DEFAULT_INSTANCE = new SingleLineLogger(LoggerFactory.getLogger(Fastload.NAMESPACE));

    public static SingleLineLogger of(Logger logger) {
        return new SingleLineLogger(logger);
    }

    public MessageContent ofType(Types type) {
        switch (type) {
            case DEBUG -> {
                return new MessageContent() {
                    @Override
                    public MessageContent withMessage(String string) {
                        logger.debug(string);
                        return this;
                    }
                };
            }

            case ERROR -> {
                return new MessageContent() {
                    @Override
                    public MessageContent withMessage(String string) {
                        logger.error(string);
                        return this;
                    }
                };
            }

            case INFO -> {
                return new MessageContent() {
                    @Override
                    public MessageContent withMessage(String string) {
                        logger.info(string);
                        return this;
                    }
                };
            }

            case WARN -> {
                return new MessageContent() {
                    @Override
                    public MessageContent withMessage(String string) {
                        logger.warn(string);
                        return this;
                    }
                };
            }
        }
        throw new NullPointerException();
    }
}
