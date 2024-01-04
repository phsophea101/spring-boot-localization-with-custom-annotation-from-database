package com.sample.spring.common.model;

public interface RecyclableEntity<T> {

    T getStatus();

    void setStatus(T status);
}
