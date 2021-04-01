package chrome.dinosaur.gamestate;

import javax.inject.Inject;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;

import chrome.dinosaur.ecs.component.*;

public class GameStart extends GameState {

    @Inject
    Engine engine;
    
    @Inject
    SpriteBatch batch;
    
    @Inject
    Viewport viewport;
    
    @Inject
    TextureRegion titleDino;
    
    @Inject
    ComponentMapper<GameStateFinishedComponent> gameStateFinishedMapper;
    
    @Inject
    public GameStart() {}

    @Override
    public void show() {
        Entity dino = new Entity()
            .add(new PositionComponent(0, 0))
            .add(new VelocityComponent(0, 0))
            .add(new PlayerComponent(false))
            .add(new TextureRegionComponent(titleDino));

        Entity gameStateFinished = new Entity()
            .add(new GameStateFinishedComponent(onFinished, false));

        engine.addEntity(dino);
        engine.addEntity(gameStateFinished);
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