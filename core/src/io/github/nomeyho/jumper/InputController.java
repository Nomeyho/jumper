package io.github.nomeyho.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class InputController implements InputProcessor {
    GameManager gameManager;

    public InputController (GameManager gameManager) {
        this.gameManager = gameManager;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.gameManager.player.setTouchedPos(toWorld(screenX, screenY));
        if(!GameManager.GAME_STARTING) {
            GameManager.GAME_STARTING = true;

        }
        this.gameManager.player.jump();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
       this.gameManager.player.setTouchedPos(toWorld(screenX, screenY));
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private Vector3 toWorld (float screenX, float screenY) {
        Vector3 touchPos = new Vector3();
        touchPos.set(screenX, screenY, 0);
        this.gameManager.camera.unproject(touchPos); // convert pixels to World units
        return touchPos;
    }
}
