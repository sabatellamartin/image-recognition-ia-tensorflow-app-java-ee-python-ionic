package com.cubit.celerity.util.yandex.params;

public enum Format {
    PLAIN("plain"),
    HTML("html");

    private final String code;

    private Format(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
