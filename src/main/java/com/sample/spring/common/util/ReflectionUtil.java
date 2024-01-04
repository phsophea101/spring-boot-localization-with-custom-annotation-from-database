package com.sample.spring.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public final class ReflectionUtil extends ReflectionUtils {


    public static List<Field> getAllFields(Class<?> clazz) {
        return getAllFields(clazz, false);
    }

    public static List<Field> getAllFields(Class<?> clazz, boolean excludeStatic) {
        return getAllFields(clazz, excludeStatic, false);
    }

    public static List<Field> getAllFields(Class<?> clazz, boolean excludeStatic, boolean onlyJavaLangType) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            List<Field> list = Arrays.asList(c.getDeclaredFields());
            if (excludeStatic)
                list = list.parallelStream().filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
            if (onlyJavaLangType)
                list = list.parallelStream().filter(field -> ClassUtil.isJavaLangType(field.getType())).collect(Collectors.toList());
            fields.addAll(list);
        }
        return fields;
    }

    public static void invokePrivateMethod(String methodName, Class<?> clazz, Object object) {
        try {
            Method m = clazz.getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static Optional<Object> safeInvokeMethod(Method method, Object target) {
        if (method == null || target == null) return Optional.empty();
        return Optional.ofNullable(invokeMethod(method, target));
    }

    public static Optional<Object> safeInvokeMethod(Method method, Object target, Object... args) {
        if (method == null || target == null) return Optional.empty();
        return Optional.ofNullable(invokeMethod(method, target, args));
    }

    public static Optional<Object> safeInvokeMethod(String methodName, Object target) {
        if (StringUtil.isEmpty(methodName) || target == null) return Optional.empty();
        Method method = findMethod(target.getClass(), methodName);
        return safeInvokeMethod(method, target);
    }

    public static Optional<Object> safeInvokeMethod(String methodName, Object target, Object... args) {
        if (StringUtil.isEmpty(methodName) || target == null) return Optional.empty();
        Method method = findMethod(target.getClass(), methodName);
        return safeInvokeMethod(method, target, args);
    }

    public static Object getDeepProperty(Object obj, String fieldName) {
        try {
            if (obj == null || StringUtil.isEmpty(fieldName)) return null;
            if (Map.class.isAssignableFrom(obj.getClass()))
                throw new UnsupportedOperationException("Cannot reflection property from " + obj.getClass().getSimpleName());
            if (fieldName.contains(".")) {
                Object value;
                String props[] = fieldName.split("\\.");
                if (List.class.isAssignableFrom(obj.getClass())) {
                    List list = (List) obj;
                    value = list.isEmpty() ? null : list.get(0);
                } else if (Set.class.isAssignableFrom(obj.getClass())) {
                    Set set = (Set) obj;
                    value = set.isEmpty() ? null : set.iterator().next();
                } else {
                    value = PropertyUtils.getProperty(obj, props[0]);
                }
                return getDeepProperty(value, fieldName.substring(props[0].length() + 1));
            } else {
                if (List.class.isAssignableFrom(obj.getClass())) {
                    List list = (List) obj;
                    return list.isEmpty() ? null : PropertyUtils.getProperty(list.get(0), fieldName);
                } else if (Set.class.isAssignableFrom(obj.getClass())) {
                    Set set = (Set) obj;
                    return set.isEmpty() ? null : PropertyUtils.getProperty(set.iterator().next(), fieldName);
                } else {
                    return PropertyUtils.getProperty(obj, fieldName);
                }
            }
        } catch (Exception e) {
            if (log.isDebugEnabled())
                log.debug(e.getMessage(), e);
            return null;
        }
    }

    public static Map<String, Object> getFieldValues(Object object, String... fields) {
        return getFieldValues(object, true, fields);
    }

    public static Map<String, Object> getFieldValues(Object object, boolean snakeCase, String... fields) {
        Map<String, Object> result = new HashMap<>();
        if (object != null || fields != null) {
            for (String field : fields) {
                String key = field;
                if (key.contains(".")) {
                    String[] split = key.split("\\.");
                    key = split[split.length - 1];
                }
                result.put(snakeCase ? StringUtil.camelCaseToSnakeCase(key) : key.trim(), getDeepProperty(object, field));
            }
        }
        return result;
    }

    public static Map<String, Field> getFields(Class<?> clazz) {
        return getFields(clazz, Object.class);
    }

    public static Map<String, Field> getFields(Class<?> clazz, Class<?> depth) {
        Map<String, Field> fieldMap = new HashMap<>();
        do {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields)
                fieldMap.put(field.getName(), field);
            clazz = clazz.getSuperclass();
        } while (clazz != null && clazz != depth);
        return fieldMap;
    }

    public static List<Field> listFields(Class<?> clazz) {
        return listFields(clazz, Object.class);
    }

    public static List<Field> listFields(Class<?> clazz, Class<?> depth) {
        return (List<Field>) getFields(clazz, depth).values();
    }

}
