package io.github.bumblesoftware.fastload.util;

@SuppressWarnings("UnusedReturnValue")
public class ExtendedString {
    private String key;

    public ExtendedString(String key) {

        this.key = key;
    }

    public ExtendedString toVar() {
        this.key = key + ": ";
        return this;
    }

    public ExtendedString addPrefix(String prefix, String discriminator) {
        this.key = prefix + discriminator + key;
        return this;
    }

    public ExtendedString addPrefix(String prefix) {
        addPrefix(prefix, "_");
        return this;
    }

    public ExtendedString addSuffix(String suffix, String discriminator) {
        this.key = key + discriminator + suffix;
        return this;
    }

    public ExtendedString addSuffix(String suffix) {
        addSuffix(suffix, "_");
        return this;
    }

    public String getString() {
        return key;
    }
}