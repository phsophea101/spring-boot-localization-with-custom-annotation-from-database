package com.sample.spring.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraceContext {

    private static final long serialVersionUID = 3683493610601948523L;

    private String traceId;
    private String parentId;
    private String spanId;
    private boolean sampled;

}
