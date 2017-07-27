package io.github.nomeyho.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.utils.PlayerEnum;

public class InputController implements InputProcessor {


    public InputController () {
        Application.get().inputMultiplexer.addProcessor(this);
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
            GameManager.get().player.state = PlayerEnum.TAKEOFF;
            GameManager.get().player.setTouchedPos(toWorld(screenX, screenY));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(GameManager.get().player.state == PlayerEnum.FLYING)
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
