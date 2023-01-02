package io.github.bumblesoftware.fastload.util.log;

import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.ExtendedString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record FastloadLogger(Logger logger) {
    public static final FastloadLogger DEFAULT_INSTANCE = new FastloadLogger(LoggerFactory.getLogger(Fastload.NAMESPACE));

    public void log(String base, MorphHelper helper, LogFinisher logFinisher) {
        new Morph(base, logger).morph(helper).log(logFinisher);
    }

    public void log(String base, LogFinisher logFinisher) {
        log(base, ExtendedString::getString, logFinisher);
    }

    public void log(String base, MorphHelper helper) {
        log(base, helper, Logger::info);
    }


    private sealed static class LogInvoker permits Morph {
        private final Logger logger;
        private final String key;

        private LogInvoker(String key, Logger logger) {
            this.key = key;
            this.logger = logger;
        }

        private void log(LogFinisher log) {
            log.log(logger, key);
        }
    }

    private static final class Morph extends LogInvoker {

        private Morph(String key, Logger logger) {
            super(key, logger);
        }

        private LogInvoker morph(MorphHelper helper) {
            return new LogInvoker(helper.morph(new ExtendedString(super.key)), super.logger);
        }
    }
}
