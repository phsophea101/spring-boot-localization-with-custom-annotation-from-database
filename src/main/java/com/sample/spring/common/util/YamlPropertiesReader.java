package com.sample.spring.common.util;

import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.core.CollectionFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

public class YamlPropertiesReader extends YamlProcessor {

    public YamlPropertiesReader() {
    }

    public YamlPropertiesReader(ClassPathResource... resource) {
        this();
        setResources(resource);
    }

    public Properties getProperties() {
        Properties result = CollectionFactory.createStringAdaptingProperties();
        process((prop, map) -> result.putAll(prop));
        return result;
    }
}
