package chrome.dinosaur.di.module;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import dagger.Module;
import dagger.Provides;

@Module(includes = AssetManagerModule.class)
public class AssetModule {
    
    @Provides
    public Sprite titleDino(AssetManager manager) {
        return manager.get("sprite.atlas", TextureAtlas.class).createSprite("title_dino");
    }
}