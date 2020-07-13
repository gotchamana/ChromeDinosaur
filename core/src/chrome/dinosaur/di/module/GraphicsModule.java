package chrome.dinosaur.di.module;

import javax.inject.Singleton;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import dagger.Module;
import dagger.Provides;

@Module
public class GraphicsModule {
    
    @Provides
    @Singleton
    public SpriteBatch spriteBatch() {
        return new SpriteBatch();
    }

    @Provides
    @Singleton
    public Viewport viewport() {
        OrthographicCamera camera = new OrthographicCamera(800, 200);
        camera.setToOrtho(false);
        return new FitViewport(800, 200, camera);
    }
}