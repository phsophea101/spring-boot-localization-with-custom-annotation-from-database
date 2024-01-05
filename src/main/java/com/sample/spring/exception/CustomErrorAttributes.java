package com.sample.spring.exception;

import com.sample.spring.conts.BizErrorCode;
import com.sample.spring.util.I18nUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Configuration;
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
        int status = (int) map.get("status");
        String message = (String) map.get("message");
        String errorMsg = (String) map.get("error");
        message = StringUtils.isEmpty(message) ? errorMsg : message;
        String code = String.format("E0%s", status);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> error = new HashMap<>();
        String description = message;
        if (exception instanceof BizException) {
            code = ((BizException) exception).getError().getValue();
            message = ((BizException) exception).getError().getDescription();
        } else if (exception instanceof Exception) {
            description = ((Exception) exception).getMessage();
        } else {
            message = I18nUtils.messageResolver(code, message);
        }
        error.putIfAbsent("code", code);
        error.putIfAbsent("message", message);
        error.putIfAbsent("description", description);
        response.putIfAbsent("status", String.valueOf(status));
        response.putIfAbsent("result", "Failed");
        if (!code.equalsIgnoreCase(BizErrorCode.E0001.getValue()))
            response.putIfAbsent("result", I18nUtils.messageResolver("FAILED", "Failed"));
        response.putIfAbsent("error", error);
        response.putIfAbsent("data", null);
        return response;
    }
}