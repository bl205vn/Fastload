package io.github.bumblesoftware.fastload.util.log;

import io.github.bumblesoftware.fastload.util.ExtendedString;

/**
 * This class is a simple functional interface that is used to call the logger.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
@FunctionalInterface
public interface MessageContent {

    /**
     * The method to be overridden in order to provide a method call;
     * @param string The message you want to print.
     */
    void execute(String string);

    /**
     * @param string The message you want to print in form of {@link ExtendedString}
     * @return The current instance to be repeatable.
     */
    default MessageContent withMessage(ExtendedString string) {
        return withMessage(string.toString());
    }

    /**
     * @param stringArray A List of messages you want to print in form of {@link ExtendedString}
     * @return The current instance to be repeatable.
     */
    default MessageContent withMessage(ExtendedString[] stringArray) {
        for (ExtendedString string : stringArray) {
            withMessage(string);
        }
        return this;
    }

    /**
     * @param stringArray A List of messages you want to print;
     * @return The current instance to be repeatable.
     */
    default MessageContent withMessage(String[] stringArray) {
        for (String string : stringArray) {
            withMessage(string);
        }
        return this;
    }

    /**
     * @param string The message you want to print.
     * @return The current instance to be repeatable.
     */
    default MessageContent withMessage(String string) {
        execute(string);
        return this;
    }

    /**
     * Prints a new empty line.
     * @return The current instance of this interface to be able to print multiple lines.
     */
    default MessageContent newLine() {
        return withMessage("");
    }

}