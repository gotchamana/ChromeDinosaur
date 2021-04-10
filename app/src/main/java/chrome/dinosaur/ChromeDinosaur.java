package chrome.dinosaur;

import javax.inject.Inject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.*;
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

    public static void main(String[] arg) {
        var config = new LwjglApplicationConfiguration();
        config.width = WIDTH;
        config.height = HEIGHT;
        config.resizable = false;

        new LwjglApplication(new ChromeDinosaur(), config);
    }

    public enum Asset {
        WHITE_BLOCK, TITLE_DINO, JUMP_DINO, FLOOR1, FLOOR2, FLOOR3, WALK_DINO1, WALK_DINO2, CROUCH_WALK_DINO1,
        CROUCH_WALK_DINO2, SMALL_CACTUS_ONE, SMALL_CACTUS_TWO, SMALL_CACTUS_THREE, LARGE_CACTUS_ONE, LARGE_CACTUS_TWO,
        LARGE_CACTUS_FOUR;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}