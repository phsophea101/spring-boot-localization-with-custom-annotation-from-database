package com.sample.spring.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InputStreamProperties extends Properties {

    public InputStreamProperties() {
        super();
    }

    public InputStreamProperties(InputStream stream) throws IOException {
        this();
        load(stream);
    }

    public InputStreamProperties(Properties defaults) {
        super(defaults);
    }
}
