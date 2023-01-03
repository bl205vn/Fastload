package io.github.bumblesoftware.fastload.util;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class ExtendedString {
    private String key;

    private ExtendedString(String key) {

        this.key = key;
    }

    public static ExtendedString of(String key) {
        return new ExtendedString(key);
    }

    public static ExtendedString[] of(ExtendedString... key) {
        return key;
    }

    public static List<ExtendedString> of(String[] key) {
        List<ExtendedString> list = new LinkedList<>();
        for (String string : key) {
            list.add(new ExtendedString(string));
        }
        return list;
    }

    public ExtendedString toVar() {
        this.key = key + ": ";
        return this;
    }

    public ExtendedString toUpperCase() {
        key = key.toUpperCase();
        return this;
    }

    public ExtendedString toLowerCase() {
        key = key.toLowerCase();
        return this;
    }

    public ExtendedString addPrefix(String prefix, String discriminator) {
        this.key = prefix + discriminator + key;
        return this;
    }

    public ExtendedString addPrefix(String prefix, char discriminator) {
        addPrefix(prefix, String.valueOf(discriminator));
        return this;
    }

    public ExtendedString addPrefix(String prefix) {
        addPrefix(prefix, "");
        return this;
    }

    public ExtendedString addSuffix(String suffix, String discriminator) {
        this.key = key + discriminator + suffix;
        return this;
    }

    public ExtendedString addSuffix(String suffix, char discriminator) {
        addSuffix(suffix, String.valueOf(discriminator));
        return this;
    }

    public ExtendedString addSuffix(String suffix) {
        addSuffix(suffix, "");
        return this;
    }

    @Override
    public String toString() {
        return key;
    }
}