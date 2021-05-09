package chrome.dinosaur.gamestate;

import static chrome.dinosaur.ChromeDinosaur.Asset.*;
import static chrome.dinosaur.config.Config.*;

import java.util.Map;

import javax.inject.Inject;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;
import chrome.dinosaur.ecs.system.ScoreSystem;
import chrome.dinosaur.ecs.system.gamestage.*;

public class GameRun extends GameStage {
    
    @Inject
    Engine engine;
    
    @Inject
    SpriteBatch batch;
    
    @Inject
    Viewport viewport;
    
    @Inject
    Map<Asset, TextureRegion> assets;

    @Inject
    ComponentMapper<GameStageFinishedComponent> gameStageFinishedMapper;

    @Inject
    public GameRun() {}

    @Override
    public void show() {
        var floor1 = new Entity()
            .add(new PositionComponent(0, 0, 0))
            .add(new VelocityComponent(INITIAL_VELOCITY, 0))
            .add(new FloorComponent())
            .add(new TextureRegionComponent(assets.get(FLOOR1)));

        var floor2 = new Entity()
            .add(new PositionComponent(WIDTH, 0, 0))
            .add(new VelocityComponent(INITIAL_VELOCITY, 0))
            .add(new FloorComponent())
            .add(new TextureRegionComponent(assets.get(FLOOR1)));

        var obstacleAsset = getRandomObstacleAsset();
        var obstacle1 = new Entity()
            .add(new PositionComponent(WIDTH * 3f, 0, 1))
            .add(new VelocityComponent(INITIAL_VELOCITY, 0))
            .add(new ShapeComponent(obstacleAsset.getShape()))
            .add(new ObstacleComponent())
            .add(new TextureRegionComponent(assets.get(obstacleAsset)));

        obstacleAsset = getRandomObstacleAsset();
        var obstacle2 = new Entity()
            .add(new PositionComponent(WIDTH * 4f, 0, 1))
            .add(new VelocityComponent(INITIAL_VELOCITY, 0))
            .add(new ShapeComponent(obstacleAsset.getShape()))
            .add(new ObstacleComponent())
            .add(new TextureRegionComponent(assets.get(obstacleAsset)));

        var gameStageFinished = new Entity()
            .add(new GameStageFinishedComponent(onFinished, false));

        engine.addEntity(floor1);
        engine.addEntity(floor2);
        engine.addEntity(obstacle1);
        engine.addEntity(obstacle2);
        engine.addEntity(gameStageFinished);
    }

    private Asset getRandomObstacleAsset() {
        var obstacleAssets = new Asset[] { SMALL_CACTUS_ONE, SMALL_CACTUS_TWO, SMALL_CACTUS_THREE, LARGE_CACTUS_ONE,
            LARGE_CACTUS_TWO, LARGE_CACTUS_FOUR };
        return obstacleAssets[MathUtils.random.nextInt(obstacleAssets.length)];
    }

    @Override
    public void hide() {
        var gameStageFinishedFamily = Family.all(GameStageFinishedComponent.class).get();
        engine.removeEntity(engine.getEntitiesFor(gameStageFinishedFamily).first());

        engine.getSystem(ScoreSystem.class).setProcessing(false);
        engine.getSystem(GameRunSystem.class).setProcessing(false);
        engine.getSystem(GameOverSystem.class).setProcessing(true);
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