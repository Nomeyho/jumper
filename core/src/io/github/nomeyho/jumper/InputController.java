package io.github.nomeyho.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class InputController implements InputProcessor {

    private boolean avoid_start_drag = false;

    public InputController () {
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
        if(!GameManager.GAME_STARTING) {
            GameManager.GAME_STARTING = true;
            GameManager.get().player.jump();
            GameManager.get().player.setTouchedPos(toWorld(screenX, screenY));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!avoid_start_drag)
            avoid_start_drag = true;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(avoid_start_drag)
           GameManager.get().player.setTouchedPos(toWorld(screenX, screenY));
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
        GameManager.get().camera.unproject(touchPos); // convert pixels to World units
        return touchPos;
    }
}
