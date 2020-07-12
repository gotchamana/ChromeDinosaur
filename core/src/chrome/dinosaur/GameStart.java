package chrome.dinosaur;

import javax.inject.Inject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameStart implements Screen {

    @Inject
    Sprite titleDino;
    
    private OrthographicCamera camera;
    private SpriteBatch batch;
    
    public GameStart() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 200);

        batch = new SpriteBatch();
    }
    
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        titleDino.draw(batch);
		batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}