package com.sample.spring.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface I18NProperty {
	/**
	 * id to find the translation
	 */
	String fieldId() default "id";
	/**
	 * type of translate
	 */
	String type();
	
	String locale() default "";
}
