package com.sample.spring.config;

import com.sample.spring.conts.BizErrorCode;
import com.sample.spring.exception.BizException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SmartLocaleResolver extends AcceptHeaderLocaleResolver {
    @SneakyThrows
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale defaultLocale = new Locale("en");
        List<Locale> supportedLocales = Arrays.asList(new Locale("km"), new Locale("en"), new Locale("kr"));
        if (StringUtils.isBlank(request.getHeader("Accept-Language"))) {
            return defaultLocale;
        }
        this.setSupportedLocales(supportedLocales);
        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader("Accept-Language"));
        Locale locale = Locale.lookup(list, getSupportedLocales());
        if (ObjectUtils.isEmpty(locale))
            throw new BizException(BizErrorCode.E0001, String.format(BizErrorCode.E0001.getDescription(), list.get(0)));
        return locale;
    }

}