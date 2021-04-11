package chrome.dinosaur.gamestate;

import static chrome.dinosaur.ChromeDinosaur.WIDTH;
import static chrome.dinosaur.ChromeDinosaur.Asset.*;

import java.util.Map;

import javax.inject.Inject;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;

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
            .add(new VelocityComponent(-10, 0))
            .add(new FloorComponent())
            .add(new TextureRegionComponent(assets.get(FLOOR1)));

        var floor2 = new Entity()
            .add(new PositionComponent(WIDTH, 0, 0))
            .add(new VelocityComponent(-10, 0))
            .add(new FloorComponent())
            .add(new TextureRegionComponent(assets.get(FLOOR1)));

        var obstacle1 = new Entity()
            .add(new PositionComponent(WIDTH, 0, 1))
            .add(new VelocityComponent(-10, 0))
            .add(new ShapeComponent(SMALL_CACTUS_ONE.getShape()))
            .add(new ObstacleComponent())
            .add(new TextureRegionComponent(assets.get(SMALL_CACTUS_ONE)));

        var obstacle2 = new Entity()
            .add(new PositionComponent(WIDTH * 2f, 0, 1))
            .add(new VelocityComponent(-10, 0))
            .add(new ShapeComponent(SMALL_CACTUS_ONE.getShape()))
            .add(new ObstacleComponent())
            .add(new TextureRegionComponent(assets.get(SMALL_CACTUS_ONE)));

        var gameStageFinished = new Entity()
            .add(new GameStageFinishedComponent(onFinished, false));

        engine.addEntity(floor1);
        engine.addEntity(floor2);
        engine.addEntity(obstacle1);
        engine.addEntity(obstacle2);
        engine.addEntity(gameStageFinished);
    }
    
    @Override
    public void hide() {
        engine.removeAllEntities();
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