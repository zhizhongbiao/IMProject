package com.example.alv_chi.improject.util;

/**
 * Created by Alv_chi on 2017/2/23.
 */

public class StringUtil {

    public static boolean isLetterString(String str) {

        return str != null&&str.matches("^[A-Za-z]+$");
    }

    public static boolean isLetter(String str) {

        return str != null&&str.matches("[a-zA-Z]");
    }
}
