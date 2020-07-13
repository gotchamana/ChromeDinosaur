package chrome.dinosaur.di.module;

import javax.inject.Singleton;

import com.badlogic.gdx.assets.AssetManager;

import dagger.Module;
import dagger.Provides;

@Module
public class AssetManagerModule {
    
    @Provides
    @Singleton
    public AssetManager assetManager() {
        return new AssetManager();
    }
}