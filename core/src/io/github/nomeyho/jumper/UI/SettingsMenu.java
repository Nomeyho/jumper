package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.I18NBundle;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageManager;


public class SettingsMenu extends Dialog implements ITranslatable {

    private TextButton saveBtn;
    private TextButton cancelBtn;

    public SettingsMenu(String title, Skin skin) {
        super(title, skin);
        this.init(skin);
    }

    public void init (Skin skin) {
        LanguageManager.get().register(this);

        this.setResizable(false);
        this.setMovable(false);
        this.setModal(true);
        this.setVisible(false);

        this.saveBtn = new TextButton("", skin);
        this.saveBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                setVisible(false);
            }
        });
        this.cancelBtn = new TextButton("", skin);
        this.cancelBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                setVisible(false);
            }
        });
        this.getButtonTable().padBottom(20);
        this.getButtonTable().add(this.saveBtn);
        this.getButtonTable().add(this.cancelBtn);

        this.updateLang();
    }

    @Override
    public void updateLang() {
        I18NBundle bundle = LanguageManager.get().getBundle();
        this.getTitleLabel().setText(bundle.get("settings"));
        this.saveBtn.setText(bundle.get("save"));
        this.cancelBtn.setText(bundle.get("cancel"));
    }
}
