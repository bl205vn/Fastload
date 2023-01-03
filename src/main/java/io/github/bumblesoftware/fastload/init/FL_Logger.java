package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.util.log.SingleLineLogger;
import org.slf4j.LoggerFactory;

/**
 * The default instance that fastload uses.
 */
public class FL_Logger {
    /**
     * The default instance that fastload uses.
     */
    public static final SingleLineLogger DEFAULT_INSTANCE = new SingleLineLogger(LoggerFactory.getLogger(Fastload.NAMESPACE));
}