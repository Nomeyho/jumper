package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.I18NBundle;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageEnum;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.sound.SoundManager;


public class SettingsMenu extends Dialog implements ITranslatable {

    private Label soundLabel;
    private Label musicLabel;
    private Label langLabel;

    private Slider soundVol;
    private Slider musicVol;

    private CheckBox soundChk;
    private CheckBox musicChk;

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

        // Sound
        this.soundLabel = new Label("", skin);
        this.soundChk = new CheckBox("", skin);
        this.soundChk.setWidth(this.soundChk.getHeight());
        this.soundVol = new Slider(0, 100, 1, false, skin);
        this.getContentTable().add(this.soundLabel);
        this.getContentTable().add(this.soundChk);
        this.getContentTable().add(this.soundVol);
        this.getContentTable().row();

        // Music
        this.musicLabel = new Label("", skin);
        this.musicChk = new CheckBox("", skin);
        this.musicChk.setWidth(this.musicChk.getHeight());
        this.musicVol = new Slider(0, 100, 1, false, skin);
        this.getContentTable().add(this.musicLabel);
        this.getContentTable().add(this.musicChk);
        this.getContentTable().add(this.musicVol);
        this.getContentTable().row();

        // Language select
        this.langLabel = new Label("", skin);
        this.langSelect = new SelectBox<LanguageEnum>(skin);
        this.langSelect.setItems(LanguageEnum.toList());
        this.getContentTable().add(this.langLabel);
        this.getContentTable().add(new Label("", skin));
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
        this.soundChk.setChecked(UserPreferences.get().sound);
        this.soundVol.setValue(UserPreferences.get().volSound);
        this.musicChk.setChecked(UserPreferences.get().music);
        this.musicVol.setValue(UserPreferences.get().volMusic);
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
        UserPreferences.get().sound = this.soundChk.isChecked();
        UserPreferences.get().volSound = (int)this.soundVol.getValue();
        UserPreferences.get().music = this.musicChk.isChecked();
        UserPreferences.get().volMusic = (int)this.musicVol.getValue();
        UserPreferences.get().lang = this.langSelect.getSelected();

        // Actually save it to the file
        UserPreferences.get().save();

        // Perform UI changes
        LanguageManager.get().setLang(UserPreferences.get().lang);
        SoundManager.get().updateVolume();
    }
}
