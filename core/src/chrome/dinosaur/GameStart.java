package chrome.dinosaur;

import javax.inject.Inject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStart extends ScreenAdapter {

    @Inject
    Sprite titleDino;
    
    @Inject
    SpriteBatch batch;
    
    @Inject
    Viewport viewport;
    
    @Override
    public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.setTransformMatrix(viewport.getCamera().view);
        batch.setProjectionMatrix(viewport.getCamera().projection);

        batch.begin();
        titleDino.setPosition(0, 0);
        titleDino.draw(batch);
		batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}