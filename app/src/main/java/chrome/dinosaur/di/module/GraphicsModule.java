package chrome.dinosaur.di.module;

import static chrome.dinosaur.config.Config.*;

import javax.inject.Singleton;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.*;

import dagger.Module;
import dagger.Provides;

@Module
public class GraphicsModule {
    
    private GraphicsModule() {}

    @Provides
    @Singleton
    public static SpriteBatch provideSpriteBatch() {
        return new SpriteBatch();
    }

    @Provides
    @Singleton
    public static Viewport provideViewport() {
        var camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.setToOrtho(false);
        return new FitViewport(WIDTH, HEIGHT, camera);
    }
}