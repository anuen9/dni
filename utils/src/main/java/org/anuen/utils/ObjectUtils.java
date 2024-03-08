package org.anuen.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class ObjectUtils {

    public static <T> Boolean isAllFieldsNonNull(T tarObj) {
        try {
            Class<?> clz = tarObj.getClass(); // get target class

            for (Field field : clz.getDeclaredFields()) { // traversal all fields
                field.setAccessible(true);

                if (field.get(tarObj) == null) { // if the value of current field are null -> false
                    return Boolean.FALSE;
                }
            }
        } catch (Exception e) {
            log.error("""
                    ---> ObjectUtils.isAllFieldsNonNull() execute fail!
                    ------> check Class: {}.
                    """, tarObj.getClass(), e);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

}
