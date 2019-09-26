package com.example.user.lankabellapps.helper;

/**
 * Created by Thejan on 7/13/2015.
 */
public class StringEmptyCheck {
    public StringEmptyCheck() {
    }

    public static boolean isNotNullNotEmptyNotWhiteSpaceOnly(String string)
    {
        return string != null && !string.isEmpty() && !string.trim().isEmpty();
    }
}
