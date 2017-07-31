package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.files.PlayerStats;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.sound.SoundEnum;
import io.github.nomeyho.jumper.sound.SoundManager;
import io.github.nomeyho.jumper.utils.Utils;

public class GameoverMenu extends Dialog implements ITranslatable {

    private Label scoreTitle;
    private Label score;
    private Label bestScore;
    private String bestScorePrefix = "";
    private TextButton restartBtn;
    private TextButton mainBtn;
    private Label tooltip;

    // Given how it is used (only in the tooltip), do not use the full registering process
    private I18NBundle bundle = LanguageManager.get().getBundle();


    public GameoverMenu(String title, Skin skin) {
        super(title, skin, "transparent");
        init(skin);
    }

    public void init (final Skin skin) {
        LanguageManager.get().register(this);

        /* Create UI elements */
        this.setResizable(false);
        this.setMovable(false);
        this.setModal(true);
        this.setFillParent(true);
        this.setVisible(false);

        this.getButtonTable().padBottom(20);
        this.getContentTable().defaults().padBottom(10); // space between rows
        this.getContentTable().setFillParent(true);

        // Title
        this.getTitleLabel().setAlignment(Align.center);

        this.scoreTitle = new Label("", skin);
        this.getContentTable().add(this.scoreTitle);
        this.getContentTable().row();

        this.score = new Label("", skin, "large");
        this.getContentTable().add(this.score).padBottom(30);
        this.getContentTable().row();

        this.bestScore = new Label("", skin, "small");
        this.getContentTable().add(this.bestScore).padBottom(150);
        this.getContentTable().row();

        this.tooltip = new Label("", skin);
        this.tooltip.setAlignment(Align.center);
        this.tooltip.setColor(Color.RED);
        this.getContentTable().add(this.tooltip).padBottom(50);
        this.getContentTable().row();

        // Restart
        this.restartBtn = new TextButton("", skin);
        this.restartBtn.getLabelCell().padBottom(7).padTop(7);
        this.restartBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                // Prevent playing without lifes
                if(PlayerStats.get().remainingLifes <= 0) {
                    tooltip.setText(bundle.get("no_more_lives"));
                    tooltip.addAction(Actions.sequence(
                            Actions.color(Color.RED),
                            Actions.fadeIn(1f),
                            Actions.fadeOut(1f)
                    ));
                    return;
                }

                super.tap(event, x, y, count, button);
                SoundManager.get().playSound(SoundEnum.CLICK);
                GameManager.get().restart(false);
                hide();
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
                SoundManager.get().playSound(SoundEnum.CLICK);
                hide();
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
        this.restartBtn.setText(bundle.get("retry"));
        this.mainBtn.setText(bundle.get("menu"));
        this.scoreTitle.setText(bundle.get("score"));
        this.bestScorePrefix = bundle.get("best_score");

        Utils.setButtonWidth(this.getContentTable());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.restartBtn.setDisabled(PlayerStats.get().remainingLifes <= 0);
    }

    public void show () {
        this.scoreTitle.setText("Score");
        this.score.setText(PlayerStats.get().currentScore + "");
        this.bestScore.setText(this.bestScorePrefix + " " + PlayerStats.get().bestScore);

        // Play sound once
        if(!this.isVisible())
            SoundManager.get().playSound(SoundEnum.GAME_OVER);

        this.setVisible(true);
    }

    public void hide () {
        setVisible(false);
    }
}
