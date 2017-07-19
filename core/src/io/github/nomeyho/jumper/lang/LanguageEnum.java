package io.github.nomeyho.jumper.lang;

import java.util.Locale;

public enum LanguageEnum {
    ENGLISH("en"),
    FRENCH("fr");

    private Locale locale;

    LanguageEnum(String locale) {
        this.locale = new Locale(locale);
    }

    public Locale getLocale() {
        return this.locale;
    }
}
