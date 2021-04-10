package chrome.dinosaur.gamestate;

import static chrome.dinosaur.ChromeDinosaur.Asset.*;

import java.util.Map;

import javax.inject.Inject;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.*;
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
    public GameStart() { }

    @Override
    public void show() {
        var floor = new Entity()
            .add(new PositionComponent(0, 0, 0))
            .add(new TextureRegionComponent(assets.get(FLOOR1)));

        var dino = new Entity()
            .add(new PositionComponent(0, 0, 2))
            .add(new VelocityComponent(0, 0))
            .add(new PlayerComponent())
            .add(new TextureRegionComponent(assets.get(JUMP_DINO)));

        var whiteBlock = new Entity()
            .add(new PositionComponent(assets.get(JUMP_DINO).getRegionWidth(), 0, 3))
            .add(new VelocityComponent(0, 0))
            .add(new TextureRegionComponent(assets.get(WHITE_BLOCK)));

        var gameStageFinished = new Entity()
            .add(new GameStageFinishedComponent(onFinished, false));

        engine.addEntity(floor);
        engine.addEntity(dino);
        engine.addEntity(whiteBlock);
        engine.addEntity(gameStageFinished);
    }
    
    @Override
    public void hide() {
        engine.getEntities().forEach(e -> {
            if (!playerMapper.has(e))
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