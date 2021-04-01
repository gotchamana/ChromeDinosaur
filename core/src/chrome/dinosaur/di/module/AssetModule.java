package chrome.dinosaur.di.module;

import javax.inject.Singleton;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.*;

import dagger.Module;
import dagger.Provides;

@Module
public class AssetModule {
    
    private AssetModule() {}

    @Provides
    @Singleton
    public static AssetManager provideAssetManager() {
        var assetManager = new AssetManager();
        assetManager.load("sprite.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        return assetManager;
    }

    @Provides
    public static TextureRegion provideTitleDino(AssetManager manager) {
        return manager.get("sprite.atlas", TextureAtlas.class).findRegion("title_dino");
    }
}