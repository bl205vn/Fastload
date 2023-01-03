package io.github.bumblesoftware.fastload.util.log;

import io.github.bumblesoftware.fastload.util.ExtendedString;
import org.slf4j.Logger;

/**
 * SingeLineLogger is used to easily print multiple messages in one line. It also supports
 * {@link ExtendedString ExtendedString}.
 */
public final class SingleLineLogger {

    /**
     * A logger to use in order to print things.
     */
    public final Logger logger;

    /**
     * Logs in debug
     */
    public final MessageContent DEBUG;

    /**
     * Logs error
     */
    public final MessageContent ERROR;

    /**
     * Logs info
     */
    public final MessageContent INFO;

    /**
     * Warns log
     */
    public final MessageContent WARN;

    /**
     * ALL TYPES
     */
    public final MessageContent[] ALL;

    /**
     * Basic constructor.
     * @param logger A logger to use in order to print things.
     */
    public SingleLineLogger(Logger logger) {
        this.logger = logger;
        DEBUG = logger::debug;
        ERROR = logger::error;
        INFO = logger::info;
        WARN = logger::warn;
        ALL = new MessageContent[] {
                DEBUG,
                ERROR,
                INFO,
                WARN
        };
    }

    /**
     * @param logger A logger to use in order to print things.
     * @return A new instance of {@link SingleLineLogger SingleLineLogger}
     */
    public static SingleLineLogger of(Logger logger) {
        return new SingleLineLogger(logger);
    }
}
