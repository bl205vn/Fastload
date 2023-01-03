package io.github.bumblesoftware.fastload.util.log;

import io.github.bumblesoftware.fastload.util.ExtendedString;

@SuppressWarnings("unused")
public interface MessageContent {

        MessageContent withMessage(String string);

        default MessageContent withMessage(ExtendedString string) {
            return withMessage(string.toString());
        }

        default MessageContent newLine() {
            return withMessage("");
        }


}