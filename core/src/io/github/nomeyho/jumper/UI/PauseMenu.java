package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.SnapshotArray;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.utils.Utils;

public class PauseMenu extends Dialog implements ITranslatable {

    private TextButton continueBtn;
    private TextButton restartBtn;
    private TextButton mainBtn;

    public PauseMenu(String title, Skin skin) {
        super(title, skin, "transparent");
        init(skin);
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

        // Title
        this.getTitleLabel().setAlignment(Align.center);

        // Continue
        this.continueBtn = new TextButton("", skin);
        this.continueBtn.getLabelCell().padBottom(7).padTop(7);
        this.continueBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                GameManager.get().resume();
                setVisible(false);
            }
        });
        this.getContentTable().add(this.continueBtn).padBottom(100);
        this.getContentTable().row();

        // Restart
        this.restartBtn = new TextButton("", skin);
        this.restartBtn.getLabelCell().padBottom(7).padTop(7);
        this.restartBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                GameManager.get().restart();
                setVisible(false);
            }
        });
        this.getContentTable().add(this.restartBtn).padBottom(100);
        this.getContentTable().row();

        // Main
        this.mainBtn = new TextButton("", skin);
        this.mainBtn.getLabelCell().padBottom(7).padTop(7);
        this.mainBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                setVisible(false);
                // Remove event listeners
                Application.get().inputMultiplexer.clear();
                // New screen
                GameManager.get().game.setScreen(new MenuScreen(GameManager.get().game));
            }
        });
        this.getContentTable().add(this.mainBtn).padBottom(100);
        this.getContentTable().row();

        // Lang
        this.updateLang();
    }

    @Override
    public void updateLang() {
        I18NBundle bundle = LanguageManager.get().getBundle();
        this.continueBtn.setText(bundle.get("continue"));
        this.restartBtn.setText(bundle.get("restart"));
        this.mainBtn.setText(bundle.get("main_menu"));

        Utils.setButtonWidth(this.getContentTable());
    }
}
