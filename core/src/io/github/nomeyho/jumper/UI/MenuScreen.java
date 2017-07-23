package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameScreen;
import io.github.nomeyho.jumper.JumperGame;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageManager;


public class MenuScreen extends ScreenAdapter implements ITranslatable {
    public static final float SIZE = Application.SIZE;
    // View
    private ExtendViewport viewport;
    private Camera camera;
    // State
    private JumperGame game;
    // UI
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Table layout;
    private Label logo;
    private TextButton playBtn;
    private TextButton buyBtn;
    private TextButton settingsBtn;
    // TODO private SettingsMenu settingsMenu;

    public MenuScreen(JumperGame game) {
        this.game = game;
        LanguageManager.get().register(this);
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        this.camera.position.set(this.camera.viewportWidth / 2,this.camera.viewportHeight /2,0);
        this.camera.update();
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer.updateMatrices();

        // TODO this.settingsMenu.setSize(this.viewport.getWorldWidth() - 10, this.viewport.getWorldHeight() - 10);
        // TODO this.settingsMenu.setPosition(centerX, centerY, Align.center);
    }

    @Override
    public void show() {
        // View
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(SIZE, SIZE, this.camera);
        this.shapeRenderer = new ShapeRenderer();
        // Layout & stage
        this.stage = new Stage(this.viewport);
        this.layout = new Table();
        this.stage.addActor(this.layout);
        this.layout.setFillParent(true);
        this.layout.align(Align.center);
        this.stage.setDebugAll(Application.DEBUG);

        Skin skin = Application.get().assetManager.get(Application.SKIN);

        // Logo
        this.logo = new Label(Application.TAG, skin, "large");
        this.layout.add(this.logo).padBottom(200);
        this.layout.row();

        // Play
        this.playBtn = new TextButton("", skin);
        this.playBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new GameScreen());
            }
        });
        this.layout.add(this.playBtn).padBottom(75);
        this.layout.row();

        // Buy
        this.buyBtn = new TextButton("", skin);
        this.layout.add(this.buyBtn).padBottom(75);
        this.layout.row();

        // Settings
        this.settingsBtn = new TextButton("", skin);
        this.settingsBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                // TODO settingsMenu.setVisible(true);
            }
        });
        this.layout.add(this.settingsBtn).padBottom(75);
        // TODO this.settingsMenu = new SettingsMenu("", skin);
        // TODO this.stage.addActor(this.settingsMenu);

        // Lang
        updateLang();
        LanguageManager.get().register(this);

        // Start taking input from the UI
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        this.stage.act(delta);
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        this.shapeRenderer.dispose();
    }

    private void update() {
        this.stage.act();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(27 / 250f, 33 / 255f, 40 / 255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT) ;
    }

    private void draw() {
        // Draw the grid
        if(Application.DEBUG)
            this.drawGrid();
        // Show the loading screen
        this.stage.draw();
    }

    private void drawGrid () {
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < this.viewport.getWorldWidth(); x += Application.CELL) {
            for (int y = 0; y < this.viewport.getWorldHeight(); y += Application.CELL) {
                this.shapeRenderer.rect(x,y, Application.CELL, Application.CELL);
            }
        }
        this.shapeRenderer.end();
    }

    public void updateLang() {
        I18NBundle bundle = LanguageManager.get().getBundle();
        this.playBtn.setText(bundle.get("play"));
        this.buyBtn.setText(bundle.get("buy"));
        this.settingsBtn.setText(bundle.get("settings"));

        // Set button width depending on the contained text
        this.setButtonWidth();
    }

    private void setButtonWidth () {
        this.layout.layout();

        // Get width
        float width = 0;
        SnapshotArray<Actor> children = this.layout.getChildren();
        Actor child;
        for(int i=0, end = children.size; i<end; ++i) {
            child = children.get(i);
            if(child instanceof TextButton) {
                TextButton button = (TextButton) child;
                if(button.getLabel().getWidth() > width)
                    width = button.getLabel().getWidth();
            }
        }

        // Padding:
        System.out.println(width);
        width += 30;

        // Set width
        Array<Cell> cells = this.layout.getCells();
        for(int i=0, end = cells.size; i<end; ++i) {
            Cell cell = cells.get(i);
            if(cell.getActor() instanceof Button)
                cell.width(width).expandX();
            this.layout.layout();
            // Effectively apply new style
            cell.getActor().setSize(width, cell.getActorHeight());
        }

        // Effectively apply new style
        this.layout.layout();
    }
}
