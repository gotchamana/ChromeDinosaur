package chrome.dinosaur.ecs.system;

import javax.inject.*;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import chrome.dinosaur.ecs.component.AnimationComponent;
import chrome.dinosaur.ecs.component.PositionComponent;
import chrome.dinosaur.ecs.component.TextureRegionComponent;

public class RenderSystem extends IteratingSystem {

    @Inject
    ComponentMapper<PositionComponent> positionMapper;
    
    @Inject
    ComponentMapper<TextureRegionComponent> textureRegionMapper;

    @Inject
    ComponentMapper<AnimationComponent> animationMapper;
    
    @Inject
    SpriteBatch batch;

    private float elapsedTime;
    
    @Inject
    public RenderSystem(@Named("render-system.priority") int priority) {
        super(Family.all(PositionComponent.class).one(TextureRegionComponent.class, AnimationComponent.class).get(),
            priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionMapper.get(entity);
        TextureRegion textureRegion = animationMapper.has(entity)
                ? animationMapper.get(entity).getAnimation().getKeyFrame(elapsedTime)
                : textureRegionMapper.get(entity).getTextureRegion();

        batch.draw(textureRegion, position.getX(), position.getY());
    }

    @Override
    public void update(float deltaTime) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        super.update(deltaTime);
        batch.end();
        
        elapsedTime += deltaTime;
    }
}