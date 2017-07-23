package io.github.nomeyho.jumper.lang;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import io.github.nomeyho.jumper.Application;

import java.util.Iterator;

public class LanguageManager {
    private static LanguageManager INSTANCE = new LanguageManager();
    private Array<ITranslatable> toTranslate = new Array<ITranslatable>();
    private I18NBundle bundle = null;
    private LanguageEnum language;

    private LanguageManager() {
        this.setLang(LanguageEnum.English);
    }

    public static LanguageManager get() {
        return INSTANCE;
    }

    public LanguageEnum getLang () {
        return this.language;
    }

    public void setLang(LanguageEnum language) {
        // Load new language file
        FileHandle handle = Gdx.files.internal(Application.LOCALES);
        this.bundle = I18NBundle.createBundle(handle, language.getLocale());

        // Translate all registered objects
        if(this.language != language) {
            for(int i=0; i<this.toTranslate.size; ++i)
                this.toTranslate.get(i).updateLang();
        }

        // Set new language
        this.language = language;
    }

    public I18NBundle getBundle() {
        return this.bundle;
    }

    /**
     * Register a new object for which the translation will be
     * triggered in case the language change.
     * @param o translatable object
     */
    public void register(ITranslatable o) {
        this.toTranslate.add(o);
    }

    // TODO use!
    public void unregister(ITranslatable o) { this.toTranslate.removeValue(o, true); }
}
