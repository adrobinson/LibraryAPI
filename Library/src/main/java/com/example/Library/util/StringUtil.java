package com.example.Library.util;

public class StringUtil {

    private StringUtil(){}

    public static String normalizeString(String string) {
        return string == null ? null : string.trim().toLowerCase();
    }
}
