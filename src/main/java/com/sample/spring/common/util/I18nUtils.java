package com.sample.spring.common.util;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public final class I18nUtils {
    private static MessageSource bundleMessage;

    private I18nUtils() {
    }

    public static void setBundleMessage(MessageSource bundleMessage) {
        I18nUtils.bundleMessage = bundleMessage;
    }

    public static String messageResolver(String code, String defaultMessage) {
        String message = bundleMessage.getMessage(code, new Object[]{}, defaultMessage, LocaleContextHolder.getLocale());
        if (ObjectUtils.isEmpty(message))
            return defaultMessage;
        return message;
    }
}
