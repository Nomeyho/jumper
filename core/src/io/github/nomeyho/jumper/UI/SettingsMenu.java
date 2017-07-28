package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageEnum;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.sound.SoundEnum;
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
        this.setFillParent(true);
        this.getButtonTable().padBottom(20);
        this.getContentTable().defaults().padBottom(50); // space between rows
        this.getContentTable().setFillParent(true);

        // Title
        this.getTitleLabel().setAlignment(Align.center);

        // Sound
        this.soundLabel = new Label("", skin);
        this.sound = new Slider(0, 100, 1, false, skin);
        this.getContentTable().add(this.soundLabel).expandX().align(Align.left).padLeft(50);
        this.getContentTable().add(this.sound).expandX().fillX().padRight(50);
        this.getContentTable().row();

        // Music
        this.musicLabel = new Label("", skin);
        this.music = new Slider(0, 100, 1, false, skin);
        this.getContentTable().add(this.musicLabel).expandX().align(Align.left).padLeft(50);
        this.getContentTable().add(this.music).expandX().fillX().padRight(50);
        this.getContentTable().row();

        // Language select
        this.langLabel = new Label("", skin);
        this.langSelect = new SelectBox<LanguageEnum>(skin);
        this.langSelect.setItems(LanguageEnum.toList());
        this.getContentTable().add(this.langLabel).align(Align.left).padLeft(50);
        this.getContentTable().add(this.langSelect).align(Align.left);
        this.getContentTable().row();

        // Save button
        this.saveBtn = new TextButton("", skin);
        this.saveBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                SoundManager.get().playSound(SoundEnum.CLICK);
                // Save new parameters and close window
                save();
                SettingsMenu.super.hide();
            }
        });
        this.saveBtn.getLabelCell().padLeft(12f).padRight(12f);
        this.getButtonTable().add(this.saveBtn).padRight(50);

        // Cancel button
        this.cancelBtn = new TextButton("", skin);
        this.cancelBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                SoundManager.get().playSound(SoundEnum.CLICK);
                SettingsMenu.super.hide();
            }
        });
        this.cancelBtn.getLabelCell().padLeft(12f).padRight(12f);
        this.getButtonTable().add(this.cancelBtn);
        this.getButtonTable().padBottom(50);

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

    private void save () {
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
