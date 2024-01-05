package com.sample.spring.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sample.spring.jackson.annotation.I18NProperty;
import com.sample.spring.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class I18NSerializer extends StdSerializer<Object> {
    private static final long serialVersionUID = -2391442805192997903L;
    private final I18NProvider provider;

    public I18NSerializer(I18NProvider provider) {
        super(Object.class);
        this.provider = provider;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String message = this.getI18NProperty(value, gen);
        if (ObjectUtils.isNotEmpty(message))
            gen.writeString(message);
        else
            gen.writeNull();
    }

    @Override
    public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        String message = this.getI18NProperty(value, gen);
        if (ObjectUtils.isNotEmpty(message))
            super.serializeWithType(message, gen, serializers, typeSer);
        else
            gen.writeNull();
    }

    private String getI18NProperty(Object value, JsonGenerator gen) {
        try {
            String fieldName = gen.getOutputContext().getCurrentName();
            Optional<ObjectMapper> bean = ContextUtil.optBean(ObjectMapper.class);
            if (bean.isPresent()) {
                PropertyNamingStrategy namingStrategy = bean.get().getPropertyNamingStrategy();
                if (ObjectUtils.isNotEmpty(namingStrategy) && PropertyNamingStrategies.SNAKE_CASE.getClass().equals(namingStrategy.getClass()))
                    fieldName = this.snakeCaseToCamelCase(fieldName);
            }
            Object obj = gen.getCurrentValue();
            I18NProperty annotation = obj.getClass().getDeclaredField(fieldName).getAnnotation(I18NProperty.class);
            String locale = annotation.locale();
            locale = StringUtils.isNotEmpty(locale) ? locale : LocaleContextHolder.getLocale().getLanguage();
            String key = (String) PropertyUtils.getProperty(obj, annotation.fieldId());
            return this.provider.getMessage(key, annotation.type(), locale.toLowerCase(), String.valueOf(value));
        } catch (Exception e) {
            log.warn("exception occurred while serializer I18N property {} of class {} {}", gen.getOutputContext().getCurrentName(), gen.getCurrentValue().getClass().getName(), e.getMessage());
            return String.valueOf(value);
        }
    }

    private String snakeCaseToCamelCase(String string) {
        if (ObjectUtils.isEmpty(string)) return string;
        StringBuilder sb = new StringBuilder(string);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '_') {
                sb.deleteCharAt(i);
                sb.replace(i, i + 1, String.valueOf(Character.toUpperCase(sb.charAt(i))));
            }
        }
        return sb.toString();
    }
}