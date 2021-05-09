package chrome.dinosaur.di.module;

import static chrome.dinosaur.config.Config.*;

import java.util.*;

import javax.inject.Singleton;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.*;

import chrome.dinosaur.ChromeDinosaur.Asset;
import dagger.Module;
import dagger.Provides;

@Module
public class AssetModule {
    
    private AssetModule() {}

    @Provides
    @Singleton
    public static AssetManager provideAssetManager() {
        var assetManager = new AssetManager();
        assetManager.load(TEXTURE_ATLAS_NAME, TextureAtlas.class);
        assetManager.load(SCORE_SOUND_NAME, Sound.class);
        assetManager.finishLoading();

        return assetManager;
    }

    @Provides
    @Singleton
    public static TextureAtlas provideTextureAtlas(AssetManager manager) {
        var textureAtlas = manager.get(TEXTURE_ATLAS_NAME, TextureAtlas.class);
        textureAtlas.addRegion(Asset.WHITE_BLOCK.toString(), createWhiteBlockTextureRegion());

        return textureAtlas;
    }

    private static TextureRegion createWhiteBlockTextureRegion() {
        var whitePixmap = new Pixmap(WIDTH, HEIGHT, Format.RGB888);
        whitePixmap.setColor(Color.WHITE);
        whitePixmap.fillRectangle(0, 0, WIDTH, HEIGHT);

        return new TextureRegion(new Texture(whitePixmap));
    }

    @Provides
    @Singleton
    public static Map<Asset, TextureRegion> provideAssets(TextureAtlas textureAtlas) {
        var map = new EnumMap<Asset, TextureRegion>(Asset.class);
        for (var asset : Asset.values())
            map.put(asset, textureAtlas.findRegion(asset.toString()));

        return Collections.unmodifiableMap(map);
    }

    @Provides
    @Singleton
    public static Sound provideScoreSound(AssetManager manager) {
        return manager.get(SCORE_SOUND_NAME, Sound.class);
    }
}