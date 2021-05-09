package chrome.dinosaur;

import static chrome.dinosaur.config.Config.*;

import javax.inject.Inject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import chrome.dinosaur.di.component.DaggerGameComponent;
import chrome.dinosaur.gamestate.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ChromeDinosaur extends Game {

    @Inject
    AssetManager assetManager;

    @Inject
    SpriteBatch batch;

    @Inject
    GameStart gameStart;

    @Inject
    GameRun gameRun;

    @Inject
    GameOver gameOver;

    @Override
    public void create() {
        DaggerGameComponent.create().chromeDinosaur(this);

        gameStart.setOnFinished(() -> setScreen(gameRun));
        gameRun.setOnFinished(() -> setScreen(gameOver));
        gameOver.setOnFinished(() -> setScreen(gameRun));
        setScreen(gameStart);
    }

    @Override
    public void dispose() {
        screen.dispose();
        assetManager.dispose();
        batch.dispose();
    }

    public static void main(String[] arg) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> log.error("Uncaught exception", throwable));

        var config = new LwjglApplicationConfiguration();
        config.width = WIDTH;
        config.height = HEIGHT;
        config.resizable = false;

        new LwjglApplication(new ChromeDinosaur(), config);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public enum Asset {
        WHITE_BLOCK, TITLE_DINO, JUMP_DINO, FLOOR1, FLOOR2, FLOOR3, WALK_DINO1, WALK_DINO2, CROUCH_WALK_DINO1,
        CROUCH_WALK_DINO2, SHOCK_DINO, GAME_OVER, RESTART_BUTTON, ZERO_DIGIT, ONE_DIGIT, TWO_DIGIT, THREE_DIGIT,
        FOUR_DIGIT, FIVE_DIGIT, SIX_DIGIT, SEVEN_DIGIT, EIGHT_DIGIT, NINE_DIGIT, HIGH_SCORE, CLOUD,
        
        SMALL_CACTUS_ONE(new float[] { 2, 50, 7, 50, 10, 30, 12, 66, 16, 68, 21, 66, 24, 38, 26, 58, 31, 
            58, 31, 37, 22, 32, 20, 3, 12, 3, 12, 24, 2, 29 }),

        SMALL_CACTUS_TWO(new float[] { 2, 50, 4, 52, 6, 50, 10, 30, 12, 66, 16, 68, 20, 65, 24, 38, 26, 58, 30, 60, 31,
            58, 39, 60, 43, 58, 46, 66, 50, 68, 55, 66, 57, 34, 60, 58, 62, 60, 64, 58, 65, 33, 56, 28, 55, 3, 46,
            3, 46, 36, 36, 40, 27, 33, 22, 32, 21, 3, 12, 3, 11, 24, 1, 30 }),

        SMALL_CACTUS_THREE(new float[] { 2, 50, 5, 52, 7, 50, 10, 30, 12, 66, 16, 68, 21, 66, 24, 38, 26, 58, 28, 60,
            31, 58, 39, 62, 41, 60, 51, 68, 55, 66, 58, 24, 63, 54, 65, 52, 70, 60, 72, 62, 75, 60, 78, 38, 80, 66,
            84, 68, 89, 66, 92, 34, 94, 58, 96, 60, 99, 58, 99, 33, 90, 28, 89, 3, 80, 3, 80, 32, 70, 37, 65, 23,
            56, 18, 55, 3, 46, 3, 46, 22, 36, 29, 27, 33, 21, 33, 21, 3, 12, 3, 12, 25, 2, 29 }),

        LARGE_CACTUS_ONE(new float[] { 2, 72, 6, 74, 11, 72, 15, 46, 18, 96, 24, 98, 31, 96, 35, 46, 38, 76, 42, 78, 
            47, 76, 47, 47, 39, 39, 32, 39, 30, 7, 18, 7, 18, 36, 8, 37, 2, 44 }),

        LARGE_CACTUS_TWO(new float[] { 2, 72, 6, 74, 10, 72, 15, 46, 18, 96, 24, 98, 31, 96, 35, 46, 38, 76, 42, 78, 
            44, 76, 52, 86, 56, 88, 60, 86, 64, 58, 68, 96, 75, 97, 80, 96, 84, 46, 88, 76, 92, 78, 97, 76, 97, 47,
            80, 38, 81, 8, 68, 8, 68, 49, 52, 57, 31, 41, 31, 7, 18, 7, 18, 36, 2, 43 }),

        LARGE_CACTUS_FOUR(new float[] { 2, 72, 6, 75, 10, 72, 14, 44, 18, 96, 24, 97, 31, 96, 34, 44, 38, 74, 41, 76,
            45, 74, 50, 64, 54, 66, 56, 64, 59, 40, 62, 92, 66, 94, 71, 92, 75, 64, 78, 82, 81, 84, 83, 82, 88, 57,
            91, 60, 98, 48, 102, 84, 106, 86, 111, 84, 115, 58, 118, 96, 124, 98, 131, 96, 135, 44, 138, 74, 143,
            76, 147, 74, 147, 47, 131, 38, 131, 5, 119, 5, 118, 48, 102, 47, 103, 30, 93, 26, 92, 5, 88, 5, 88, 24,
            75, 31, 70, 5, 62, 5, 61, 33, 47, 44, 31, 38, 30, 5, 17, 5, 17, 34, 2, 43 });

        private float[] vertices;

        public Polygon getShape() {
            return vertices == null ? new Polygon() : new Polygon(vertices.clone());
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}