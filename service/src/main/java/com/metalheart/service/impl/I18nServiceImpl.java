package com.metalheart.service.impl;

import com.metalheart.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

@Service
public class I18nServiceImpl implements I18nService {

    @Autowired
    private MessageSourceAccessor messageAccessor;

    @Override
    public String translate(KEY key) {
        return messageAccessor.getMessage(key.getKey(), LocaleContextHolder.getLocale());
    }
}
