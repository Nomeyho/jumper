package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.I18NBundle;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageEnum;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.sound.SoundManager;


public class SettingsMenu extends Dialog implements ITranslatable {

    private Label soundLabel;
    private Label musicLabel;
    private Label langLabel;

    private Slider sound;
    private Slider music;

    private SelectBox<LanguageEnum> langSelect;

    private TextButton saveBtn;
    private TextButton cancelBtn;

    public SettingsMenu(String title, Skin skin) {
        super(title, skin);
        this.init(skin);
    }

    public void init (Skin skin) {
        LanguageManager.get().register(this);

        /* Create UI elements */
        this.setResizable(false);
        this.setMovable(false);
        this.setModal(true);
        this.setVisible(false);
        this.getButtonTable().padBottom(20);
        this.getContentTable().defaults().padBottom(10); // space between rows
        this.getContentTable().setFillParent(true);

        // Sound
        this.soundLabel = new Label("", skin);
        this.sound = new Slider(0, 100, 1, false, skin);
        this.getContentTable().add(this.soundLabel);
        this.getContentTable().add(this.sound);
        this.getContentTable().row();

        // Music
        this.musicLabel = new Label("", skin);
        this.music = new Slider(0, 100, 1, false, skin);
        this.getContentTable().add(this.musicLabel).expandX();
        this.getContentTable().add(this.music).expandX().fillX();
        this.getContentTable().row();

        // Language select
        this.langLabel = new Label("", skin);
        this.langSelect = new SelectBox<LanguageEnum>(skin);
        this.langSelect.setItems(LanguageEnum.toList());
        this.getContentTable().add(this.langLabel);
        this.getContentTable().add(this.langSelect);
        this.getContentTable().row();

        // Save button
        this.saveBtn = new TextButton("", skin);
        this.saveBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                // Save new parameters and close window
                save();
                setVisible(false);
            }
        });
        this.getButtonTable().add(this.saveBtn);

        // Cancel button
        this.cancelBtn = new TextButton("", skin);
        this.cancelBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                setVisible(false);
            }
        });
        this.getButtonTable().add(this.cancelBtn);

        // Lang
        this.updateLang();

        /* Bind values to UI elements */
        this.sound.setValue(UserPreferences.get().sound);
        this.music.setValue(UserPreferences.get().music);
        this.langSelect.setSelected(UserPreferences.get().lang);
    }

    @Override
    public void updateLang() {
        I18NBundle bundle = LanguageManager.get().getBundle();
        this.getTitleLabel().setText(bundle.get("settings"));
        this.soundLabel.setText(bundle.get("sound"));
        this.musicLabel.setText(bundle.get("music"));
        this.langLabel.setText(bundle.get("lang"));
        this.saveBtn.setText(bundle.get("save"));
        this.cancelBtn.setText(bundle.get("cancel"));
    }

    public void save () {
        UserPreferences.get().sound = (int)this.sound.getValue();
        UserPreferences.get().music = (int)this.music.getValue();
        UserPreferences.get().lang = this.langSelect.getSelected();

        // Actually save it to the file
        UserPreferences.get().save();

        // Perform UI changes
        LanguageManager.get().setLang(UserPreferences.get().lang);
        SoundManager.get().updateVolume();
    }
}
