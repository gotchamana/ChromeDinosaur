package chrome.dinosaur.gamestate;

import static chrome.dinosaur.ChromeDinosaur.*;
import static chrome.dinosaur.ChromeDinosaur.Asset.*;

import java.util.Map;

import javax.inject.Inject;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.viewport.Viewport;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;
import chrome.dinosaur.ecs.system.gamestage.*;

public class GameStart extends GameStage {

    @Inject
    Engine engine;
    
    @Inject
    SpriteBatch batch;
    
    @Inject
    Viewport viewport;
    
    @Inject
    Map<Asset, TextureRegion> assets;

    @Inject
    ComponentMapper<PlayerComponent> playerMapper;

    @Inject
    ComponentMapper<GameStageFinishedComponent> gameStageFinishedMapper;
    
    @Inject
    ComponentMapper<ScoreComponent> scoreMapper;

    @Inject
    public GameStart() { }

    @Override
    public void show() {
        var floor = new Entity()
            .add(new PositionComponent(0, 0, 0))
            .add(new TextureRegionComponent(assets.get(FLOOR1)));

        var normalShape = new Polygon(new float[] { 5, 60, 20, 44, 44, 60, 47, 84, 79, 84, 83, 70, 75,
            60, 60, 56, 67, 48, 60, 34, 48, 24, 50, 4, 44, 4, 37, 20, 30, 4, 23, 4, 23, 20, 4, 36 });
        var crouchShape = new Polygon(new float[] { 4, 56, 114, 55, 113, 33, 105, 25, 69, 23, 69, 15, 62, 15, 56, 23,
            45, 5, 38, 5, 33, 13, 26, 13, 24, 25, 4, 45 });
        var dino = new Entity()
            .add(new PositionComponent(0, 0, 2))
            .add(new VelocityComponent(0, 0))
            .add(new ShapeComponent(normalShape))
            .add(new PlayerComponent(normalShape, crouchShape))
            .add(new TextureRegionComponent(assets.get(JUMP_DINO)));

        var whiteBlock = new Entity()
            .add(new PositionComponent(assets.get(JUMP_DINO).getRegionWidth(), 0, 3))
            .add(new VelocityComponent(0, 0))
            .add(new TextureRegionComponent(assets.get(WHITE_BLOCK)));

        var gameStageFinished = new Entity()
            .add(new GameStageFinishedComponent(onFinished, false));

        var score = new Entity()
            .add(new PositionComponent(WIDTH - DIGIT_WIDTH * 6f, HEIGHT - DIGIT_WIDTH * 2f, 2))
            .add(new ScoreComponent());

        engine.addEntity(floor);
        engine.addEntity(dino);
        engine.addEntity(whiteBlock);
        engine.addEntity(gameStageFinished);
        engine.addEntity(score);
    }
    
    @Override
    public void hide() {
        engine.getEntities().forEach(e -> {
            if (!playerMapper.has(e) && !scoreMapper.has(e))
                engine.removeEntity(e);
        });

        engine.getSystem(GameStartSystem.class).setProcessing(false);
        engine.getSystem(GameRunSystem.class).setProcessing(true);
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