package com.sample.spring.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.base.CaseFormat;
import com.sample.spring.conts.BizErrorCode;
import com.sample.spring.util.ContextUtil;
import com.sample.spring.web.vo.response.ResponseErrorVo;
import com.sample.spring.web.vo.response.ResponseVOBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;


@RestControllerAdvice
@Slf4j
public class RestExceptionHandlerAdvice {

    @SneakyThrows
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> responseException(Exception ex) {
        log.error("Exception {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ResponseVOBuilder<>().error(new ResponseErrorVo(BizErrorCode.E0000.getValue(), BizErrorCode.E0000.getDescription(), ex.getLocalizedMessage())).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<Object> responseBizException(BizException ex) {
        log.error("BizException {}", ex.getMessage(), ex);
        String message = ObjectUtils.isEmpty(ex.getDescription()) ? ex.getError().getDescription() : ex.getDescription();
        return new ResponseEntity<>(new ResponseVOBuilder<>().error(new ResponseErrorVo(ex.getError().getValue(), message)).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> responseValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException {}", ex.getMessage(), ex);
        List<Object> errors = this.processErrors(ex);
        return new ResponseEntity<>(new ResponseVOBuilder<>().error(new ResponseErrorVo(BizErrorCode.E0003.getValue(), BizErrorCode.E0003.getDescription(), errors)).build(), HttpStatus.BAD_REQUEST);
    }

    private List<Object> processErrors(MethodArgumentNotValidException e) {
        List<Object> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Map<String, String> error = new HashMap<>();
            String fieldName = this.toCase(fieldError.getField());
            error.putIfAbsent(this.toCase("validationType"), String.format("@%s", fieldError.getCode()));
            error.putIfAbsent(this.toCase("validationMessage"), String.format("Field `%s` %s", fieldName, fieldError.getDefaultMessage()));
            error.putIfAbsent(this.toCase("validationFromClass"), String.format("%s.class", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldError.getObjectName())));
            errors.add(error);
        });
        return errors;
    }

    private String toCase(String string) {
        Optional<ObjectMapper> bean = ContextUtil.optBean(ObjectMapper.class);
        if (bean.isEmpty())
            return string;
        PropertyNamingStrategy namingStrategy = bean.get().getPropertyNamingStrategy();
        if (ObjectUtils.isNotEmpty(namingStrategy) && PropertyNamingStrategies.SNAKE_CASE.getClass().equals(namingStrategy.getClass()))
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
        return string;
    }
}
