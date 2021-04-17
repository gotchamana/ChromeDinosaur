package chrome.dinosaur.gamestate;

import static chrome.dinosaur.ChromeDinosaur.*;
import static chrome.dinosaur.ChromeDinosaur.Asset.*;

import java.util.Map;

import javax.inject.Inject;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;
import chrome.dinosaur.ecs.system.gamestage.*;

public class GameOver extends GameStage {
    
    @Inject
    Engine engine;
    
    @Inject
    SpriteBatch batch;
    
    @Inject
    Viewport viewport;

    @Inject
    Map<Asset, TextureRegion> assets;

    private Entity gameOver;
    private Entity restartBtn;
    private Entity gameStageFinished;

    @Inject
    public GameOver() {}

    @Override
    public void show() {
        var gameOverTexture = assets.get(GAME_OVER);
        gameOver = new Entity()
            .add(new PositionComponent((WIDTH - gameOverTexture.getRegionWidth()) / 2f, HEIGHT * 2 / 3f, 2))
            .add(new TextureRegionComponent(gameOverTexture));

        var restartBtnTexture = assets.get(RESTART_BUTTON);
        restartBtn = new Entity()
            .add(new PositionComponent((WIDTH - restartBtnTexture.getRegionWidth()) / 2f, HEIGHT / 5f, 2))
            .add(new TextureRegionComponent(restartBtnTexture))
            .add(new RestartButtonComponent());

        gameStageFinished = new Entity()
            .add(new GameStageFinishedComponent(onFinished, false));
        
        engine.addEntity(gameOver);
        engine.addEntity(restartBtn);
        engine.addEntity(gameStageFinished);
    }

    @Override
    public void hide() {
        engine.removeEntity(gameOver);
        engine.removeEntity(restartBtn);
        engine.removeEntity(gameStageFinished);

        engine.getSystem(GameRunSystem.class).setProcessing(true);
        engine.getSystem(GameOverSystem.class).setProcessing(false);
    }

    @Override
    public void render(float delta) {
        update();
        engine.update(delta);
    }

    private void update() {
        batch.setTransformMatrix(viewport.getCamera().view);
        batch.setProjectionMatrix(viewport.getCamera().projection);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}