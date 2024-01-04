package com.sample.spring.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

public class CustomFileUtil extends FileUtils {

    public static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static Properties readYaml(String path) {
        return readYaml(new ClassPathResource(path));
    }

    public static Properties readYaml(ClassPathResource resource) {
        return new YamlPropertiesReader(resource).getProperties();
    }
}
