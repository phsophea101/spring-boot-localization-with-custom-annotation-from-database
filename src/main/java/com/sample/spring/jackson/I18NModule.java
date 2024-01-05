package com.sample.spring.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.sample.spring.jackson.annotation.I18NProperty;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;

import java.util.List;

public class I18NModule extends SimpleModule {
    private static final long serialVersionUID = 8750960660810211977L;
    private final I18NSerializer i18NSerializer;
    public I18NModule(I18NProvider provider) {
        Assert.notNull(provider, "I18N provider must not be null");
        this.i18NSerializer = new I18NSerializer(provider);
    }
    @Override
    public void setupModule(SetupContext context) {
        context.addBeanSerializerModifier(new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                for (BeanPropertyWriter writer : beanProperties) {
                    if (ObjectUtils.isNotEmpty(writer.getAnnotation(I18NProperty.class))) {
                        writer.assignSerializer(i18NSerializer);
                        writer.assignNullSerializer(i18NSerializer);
                    }
                }
                return beanProperties;
            }
        });
    }
}
