package com.sample.spring.common.exception;

import com.sample.spring.common.util.ContextUtil;
import com.sample.spring.common.util.I18nUtils;
import com.sample.spring.enums.SystemType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, options);
        Object exception = webRequest.getAttribute(ErrorAttributes.ERROR_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        int status = (int) map.get(SystemType.STATUS.getValue());
        String message = (String) map.get(SystemType.MESSAGE.getValue());
        String path = (String) map.get(SystemType.PATH.getValue());
        message = StringUtils.isEmpty(message) ? (String) map.get(SystemType.ERROR.getValue()) : message;
        String code = String.format("E0%s", status);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> error = new HashMap<>();
        Map<String, Object> errorDetail = new HashMap<>();
        errorDetail.put(SystemType.DESCRIPTION.getValue(), message);
        if (exception instanceof BizException) {
            code = ((BizException) exception).getError().getValue();
            message = ((BizException) exception).getError().getDescription();
        } else if ((exception instanceof Exception)) {
            String localizedMessage = ((Exception) exception).getLocalizedMessage();
            if (ObjectUtils.isNotEmpty(localizedMessage))
                errorDetail.put(SystemType.MESSAGE.getValue(), localizedMessage);
            errorDetail.put(SystemType.EXCEPTION.getValue(), ((Exception) exception).getClass().getSimpleName());
        } else if (HttpStatus.NOT_FOUND.value() == status)
            errorDetail.put(SystemType.EXCEPTION.getValue(), InvalidEndpointRequestException.class.getSimpleName());
        else if (HttpStatus.UNAUTHORIZED.value() == status)
            errorDetail.put(SystemType.EXCEPTION.getValue(), "UnauthorizedClientException");
        if (!errorDetail.get(SystemType.DESCRIPTION.getValue()).toString().endsWith("."))
            errorDetail.put(SystemType.DESCRIPTION.getValue(), errorDetail.get(SystemType.DESCRIPTION.getValue()) + ".");
        message = I18nUtils.messageResolver(code, message);
        error.put(SystemType.CODE.getValue(), code);
        error.put(SystemType.MESSAGE.getValue(), message);
        errorDetail.put(SystemType.PATH.getValue(), path);
        response.put(SystemType.ERROR.getValue(), error);
        if (ContextUtil.isProfileTesting()) {
            errorDetail.putAll(error);
            response.put(SystemType.ERROR.getValue(), errorDetail);
        }
        ContextUtil.getTraceContext().ifPresent(v -> response.put(SystemType.TRACE_ID.getValue(), v.getTraceId()));
        response.put(SystemType.STATUS.getValue(), String.valueOf(status));
        response.put(SystemType.RESULT.getValue(), I18nUtils.messageResolver(SystemType.FAILED.name(), SystemType.FAILED.getValue()));
        return response;
    }
}