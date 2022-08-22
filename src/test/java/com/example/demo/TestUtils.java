package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String filedName, Object toInject) {
        boolean isPrivate = false;
        try {
            Field f = target.getClass().getDeclaredField(filedName);

            if (!f.isAccessible()) { // checking if field is private
                f.setAccessible(true);
                isPrivate = true;
            }
            f.set(target, toInject);
            if (isPrivate) {
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
