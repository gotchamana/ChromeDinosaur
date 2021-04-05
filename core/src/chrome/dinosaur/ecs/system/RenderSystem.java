package chrome.dinosaur.ecs.system;

import java.util.Comparator;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;

import chrome.dinosaur.ecs.component.*;

public class RenderSystem extends SortedIteratingSystem {

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
            Comparator.comparingDouble(e -> e.getComponent(PositionComponent.class).getZ()), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var position = positionMapper.get(entity);
        var textureRegion = animationMapper.has(entity)
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