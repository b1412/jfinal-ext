package com.jfinal.ext.kit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jfinal.log.Logger;

/**
 * 利用反射操作对象的工具类
 * 
 * @author kid
 */
public class BeanKit {

    protected final static Logger logger = Logger.getLogger(BeanKit.class);

    /**
     * 给对象的字段设置值
     * 
     * @param obj 需要设值的对象
     * @param fieldName 字段名
     * @param val 字段值
     */
    public static void set(Object obj, String fieldName, Object val) {
        Field field = null;
        try {
            field = BeanKit.getDeclaredField(obj.getClass(), fieldName);
            field.setAccessible(true);
            field.set(obj, val);
            field.setAccessible(false);
        } catch (Exception e) {
            logger.error("set error", e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T get(Class clazz, Object obj, String fieldName) {
        Object result = null;
        try {
            Field field = BeanKit.getDeclaredField(clazz, fieldName);
            field.setAccessible(true);
            result = field.get(obj);
            field.setAccessible(false);
        } catch (Exception e) {
            logger.error("set error", e);
        }
        return (T) result;
    }

    public static <T> T get(Object obj, String fieldName) {
        return get(obj.getClass(), obj, fieldName);
    }

    @SuppressWarnings("unchecked")
    public static Method getDeclaredMethod(Class clazz, String methodName, Class... parameterTypes) {
        Method method = null;
        while (clazz.getSuperclass() != null) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
                continue;
            } catch (SecurityException e) {
                clazz = clazz.getSuperclass();
                continue;
            }
            break;
        }
        return method;
    }

    public static Field getDeclaredField(Class clazz, String fieldName) {
        Field field = null;
        while (clazz.getSuperclass() != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (SecurityException e) {
                clazz = clazz.getSuperclass();
                continue;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
                continue;
            }
            break;
        }
        return field;
    }
}
