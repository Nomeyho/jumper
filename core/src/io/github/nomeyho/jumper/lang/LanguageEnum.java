package io.github.nomeyho.jumper.lang;

import com.badlogic.gdx.utils.Array;

import java.util.Locale;

public enum LanguageEnum {
    English("en"),
    Francais("fr");

    private Locale locale;

    LanguageEnum(String locale) {
        this.locale = new Locale(locale);
    }

    public Locale getLocale() {
        return this.locale;
    }

    // Chapter 7, p. 257 of "Learning LibGDX Development" 2nd Edition
    public static Array<LanguageEnum> toList() {
        Array<LanguageEnum> ret = new Array<LanguageEnum>();

        for(LanguageEnum lang: LanguageEnum.values())
            ret.add(lang);

        return ret;
    }
}
