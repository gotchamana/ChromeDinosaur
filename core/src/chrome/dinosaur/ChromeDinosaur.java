package chrome.dinosaur;

import javax.inject.Inject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import chrome.dinosaur.di.component.DaggerGameComponent;
import chrome.dinosaur.gamestate.*;

public class ChromeDinosaur extends Game {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 300;

    public static final String TEXTURE_ATLAS_NAME = "sprite.atlas";

    @Inject
    AssetManager assetManager;

    @Inject
    SpriteBatch batch;
    
    @Inject
    GameStart gameStart;

    @Inject
    GameRun gameRun;

    @Override
    public void create() {
        DaggerGameComponent.create().chromeDinosaur(this);

        gameStart.setOnFinished(() -> setScreen(gameRun));
        gameRun.setOnFinished(() -> {});
        setScreen(gameStart);
    }

    @Override
    public void dispose() {
        screen.dispose();
        assetManager.dispose();
        batch.dispose();
    }

    public enum Asset {
        WHITE_BLOCK, TITLE_DINO, JUMP_DINO, FLOOR1, FLOOR2, FLOOR3, WALK_DINO1, WALK_DINO2;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}