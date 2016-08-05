package com.centit.framework.core.controller;

import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

public class StringPropertiesEditor extends PropertiesEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(trimWhile) {
            text = StringUtils.trimWhitespace(text);
        }
        setValue(StringUtils.hasText(text) ? HtmlUtils.htmlEscape(text) : text);
    }

    @Override
    public String getAsText() {
        return StringUtils.hasText((String) getValue()) ? String.valueOf(getValue()) : (String) getValue();
    }

    private boolean trimWhile;

    public void setTrimWhile(boolean trimWhile) {
        this.trimWhile = trimWhile;
    }

    public StringPropertiesEditor(boolean trimWhile) {
        this.trimWhile = trimWhile;
    }

    public StringPropertiesEditor() {
    }
}
