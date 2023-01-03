package io.github.bumblesoftware.fastload.util.log;

import io.github.bumblesoftware.fastload.util.ExtendedString;

/**
 * This class is a simple functional interface that is used to call the logger.
 */
@SuppressWarnings("unused")
public interface MessageContent {

    /**
     * @param string The message you want to print.
     * @return The current instance of this interface to be able to print multiple lines.
     */
    MessageContent withMessage(String string);

    /**
     * @param string An instance of {@link ExtendedString }
     * @return The current instance of this interface to be able to print multiple lines.
     */
    default MessageContent withMessage(ExtendedString string) {
            return withMessage(string.toString());
        }

    /**
     * Prints a new empty line.
     * @return The current instance of this interface to be able to print multiple lines.
     */
    default MessageContent newLine() {
        return withMessage("");
    }

}