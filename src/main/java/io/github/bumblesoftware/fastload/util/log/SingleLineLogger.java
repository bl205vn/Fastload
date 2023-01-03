package io.github.bumblesoftware.fastload.util.log;

import io.github.bumblesoftware.fastload.init.Fastload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SingeLineLogger is used to easily print multiple messages in one line. It also supports
 * {@link io.github.bumblesoftware.fastload.util.ExtendedString ExtendedString}.
 * @param logger A logger to use in order to print things.
 */
public record SingleLineLogger(Logger logger) {

    /**
     * The default instance that fastload uses.
     */
    public static SingleLineLogger DEFAULT_INSTANCE = new SingleLineLogger(LoggerFactory.getLogger(Fastload.NAMESPACE));

    /**
     *
     * @param logger A logger to use in order to print things.
     * @return A new instance of {@link io.github.bumblesoftware.fastload.util.log.SingleLineLogger SingleLineLogger}
     */
    public static SingleLineLogger of(Logger logger) {
        return new SingleLineLogger(logger);
    }

    /**
     *
     * @param type A {@link Types Type} of message that will be printed.
     * @return An instance of {@link MessageContent} that correlates with {@link Types}
     */
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
