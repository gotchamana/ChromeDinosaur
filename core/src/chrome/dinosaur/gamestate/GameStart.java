package chrome.dinosaur.gamestate;

import javax.inject.Inject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStart extends GameState {

    @Inject
    Sprite titleDino;
    
    @Inject
    SpriteBatch batch;
    
    @Inject
    Viewport viewport;
    
    private void update(float delta) {
        batch.setTransformMatrix(viewport.getCamera().view);
        batch.setProjectionMatrix(viewport.getCamera().projection);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Gdx.app.log("INFO", delta + ": Press UP");
        }

        titleDino.setPosition(0, 0);
    }
    
    @Override
    public void render(float delta) {
        update(delta);

		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        titleDino.draw(batch);
		batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}