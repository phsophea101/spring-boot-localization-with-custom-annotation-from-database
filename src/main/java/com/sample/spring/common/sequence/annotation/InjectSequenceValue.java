package com.sample.spring.common.sequence.annotation;

import com.sample.spring.common.sequence.SequenceType;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectSequenceValue {

    SequenceType sequenceType() default SequenceType.SEQUENCE;

    String sequencePrefix() default StringUtils.EMPTY;

    int sequenceDigit() default 9;
}
