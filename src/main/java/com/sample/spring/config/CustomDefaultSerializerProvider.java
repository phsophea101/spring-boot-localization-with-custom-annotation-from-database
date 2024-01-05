package com.sample.spring.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class CustomDefaultSerializerProvider extends DefaultSerializerProvider {
    public CustomDefaultSerializerProvider() {
        super();
    }
    public CustomDefaultSerializerProvider(CustomDefaultSerializerProvider src) {
        super(src);
    }
    protected CustomDefaultSerializerProvider(CustomDefaultSerializerProvider provider, SerializationConfig config, SerializerFactory jsf) {
        super(provider, config, jsf);
    }
    @Override
    public CustomDefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new CustomDefaultSerializerProvider(this, config, jsf);
    }
    @Override
    public DefaultSerializerProvider copy() {
        if (getClass() != CustomDefaultSerializerProvider.class)
            return super.copy();
        return new CustomDefaultSerializerProvider(this);
    }
    @Override
    public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
        if (String.class.isAssignableFrom(property.getType().getRawClass()))
            return new DefaultJsonSerializer(Strings.EMPTY);
        else if (this.isClassDecimal(property.getType().getRawClass()))
            return new DefaultJsonSerializer(BigDecimal.valueOf(BigDecimal.ZERO.doubleValue()));
        else if (Number.class.isAssignableFrom(property.getType().getRawClass()))
            return new DefaultJsonSerializer(BigInteger.ZERO);
        else if (Boolean.class.isAssignableFrom(property.getType().getRawClass()))
            return new DefaultJsonSerializer(Boolean.FALSE);
        else if (this.isClassDate(property.getType().getRawClass()))
            return new DefaultJsonSerializer(null);
        else if (Collection.class.isAssignableFrom(property.getType().getRawClass()))
            return new DefaultJsonSerializer(new ArrayList<>());
        else if (Object.class.isAssignableFrom(property.getType().getRawClass()))
            return new DefaultJsonSerializer(null);
        else
            return super.findNullValueSerializer(property);
    }
    private boolean isClassDate(Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz) || LocalDate.class.isAssignableFrom(clazz) || LocalDateTime.class.isAssignableFrom(clazz);
    }
    private boolean isClassDecimal(Class<?> clazz) {
        return Float.class.isAssignableFrom(clazz) || BigDecimal.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz);
    }
    private static class DefaultJsonSerializer extends JsonSerializer<Object> {
        private final Object defaultValue;
        public DefaultJsonSerializer(Object defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeObject(this.defaultValue);
        }
    }
}