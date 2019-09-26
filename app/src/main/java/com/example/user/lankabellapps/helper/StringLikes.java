package com.example.user.lankabellapps.helper;

/**
 * Created by Thejan on 6/2/2016.
 */
public class StringLikes {

    public static  boolean like(String str, String expr) {
        boolean matches = false;
        expr = expr.toLowerCase();
        expr = expr.replace(".", "\\.");
        expr = expr.replace("?", ".");
        expr = expr.replace("%", ".*");
        str = str.toLowerCase();
        try {
            matches = str.matches(".*" + expr + ".*");
        } catch (Exception e) {
            return false;
        }
        return matches;
    }
}
