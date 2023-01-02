package io.github.bumblesoftware.fastload.util.log;

import org.slf4j.Logger;

@FunctionalInterface
public interface LogFinisher {
    void log(Logger logger, String toLog);

}
