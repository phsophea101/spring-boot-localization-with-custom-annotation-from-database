package com.sample.spring.common.concurrent;

import com.sample.spring.common.type.TypeReference;
import com.sample.spring.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ObjectUtils {

    @SuppressWarnings("unchecked")
    static <T> T instance(Class<T> clazz) {
        try {
            return (T) clazz.getConstructors()[0].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    static boolean isString(Object obj) {
        return obj instanceof String;
    }

    static boolean isInteger(Object obj) {
        return obj instanceof Integer;
    }

    static boolean isFloat(Object obj) {
        return obj instanceof Float;
    }

    static boolean isDouble(Object obj) {
        return obj instanceof Double;
    }

    static boolean isShort(Object obj) {
        return obj instanceof Short;
    }

    static boolean isByte(Object obj) {
        return obj instanceof Byte;
    }

    static boolean isDate(Object obj) {
        return obj instanceof Date;
    }

    static boolean isLocalDate(Object obj) {
        return obj instanceof LocalDate;
    }

    static boolean isBoolean(Object obj) {
        return obj instanceof Boolean;
    }

    static Object safeConvert(Object obj, Class<?> wantTo) {
        if (obj == null)
            return null;
        if (String.class.equals(wantTo))
            return String.valueOf(obj);
        else if (Integer.class.equals(wantTo))
            return obj instanceof String ? Integer.parseInt(obj.toString()) : 0;
        else if (Float.class.equals(wantTo))
            return obj instanceof String ? Float.parseFloat(obj.toString()) : 0f;
        else if (Double.class.equals(wantTo))
            return obj instanceof String ? Double.parseDouble(obj.toString()) : 0d;
        else if (Short.class.equals(wantTo))
            return obj instanceof String ? Short.valueOf(obj.toString()) : 0;
        else if (Byte.class.equals(wantTo))
            return obj instanceof String ? Byte.valueOf(obj.toString()) : 0;
        else if (Timestamp.class.equals(wantTo))
            return isLocalDate(obj) ? DateUtil.asTimeStamp((LocalDate) obj) : deepClone((Serializable) obj);
        else if (Date.class.equals(wantTo))
            return isLocalDate(obj) ? DateUtil.asDate((LocalDate) obj) : deepClone((Serializable) obj);
        return deepClone((Serializable) obj);
    }

    static Class<?> getClassOf(Object obj) {
        if (obj == null)
            return null;
        if (isString(obj))
            return String.class;
        else if (isInteger(obj))
            return Integer.class;
        else if (isFloat(obj))
            return Float.class;
        else if (isDouble(obj))
            return Double.class;
        else if (isShort(obj))
            return Short.class;
        else if (isByte(obj))
            return Byte.class;
        return obj.getClass();
    }

    @SuppressWarnings("unchecked")
    static <T> T deepClone(Serializable obj) {
        return (T) SerializationUtils.clone(obj);
    }

    @SuppressWarnings("unchecked")
    /*static <T> T deepClone(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(bais);
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    static byte[] toByteArray(Object obj) {
        Assert.notNull(obj, "Object for serial to bytes can not be null");
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException ex) {
            log.error("Failed to serial object [" + obj.getClass().getSimpleName() + "]", ex);
            log.error("Message " + ex.getMessage(), ex);
            throw new SerializationException(ex);
        }
    }

    static Object toObject(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            log.error("Failed to deserial bytes to object", ex);
            log.error("Message " + ex.getMessage(), ex);
            throw new SerializationException(ex);
        }
    }

    static Serializable[] array(Serializable... values) {
        List<Serializable> list = new ArrayList<>();
        for (Serializable value : values)
            if (Objects.nonNull(value))
                list.add(value);
        return list.toArray(new Serializable[0]);
    }

    static <T> T cast(final Object obj, final Class<T> _class) {
        T result = null;

        if (isNotNull(obj)) {
            if (_class.isInstance(obj)) {
                result = _class.cast(obj);
            } else {
                throw new ClassCastException(
                        String.format("class %s cannot be cast to class %s",
                                obj.getClass().getName(),
                                _class.getName()));
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    static <T> T cast(final Object obj, final TypeReference<T> valueTypeRef) {
        T result = null;

        if (isNotNull(obj)) {
            Type type = valueTypeRef.getType();
            if (type instanceof ParameterizedType) {
                result = cast(obj, ((Class<T>) ((ParameterizedType) valueTypeRef.getType()).getRawType()));
            } else {
                result = cast(obj, (Class<T>) valueTypeRef.getType());
            }
        }

        return result;
    }

    static boolean isNull(final Object obj) {
        return obj == null;
    }

    static boolean isNotNull(final Object obj) {
        return !isNull(obj);
    }
}

