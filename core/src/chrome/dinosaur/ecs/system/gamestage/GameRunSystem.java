package chrome.dinosaur.ecs.system.gamestage;

import static chrome.dinosaur.ChromeDinosaur.WIDTH;
import static chrome.dinosaur.ChromeDinosaur.Asset.*;

import java.util.*;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;

public class GameRunSystem extends EntitySystem {
    
    @Inject
    Map<Asset, TextureRegion> assets;

    @Inject
    ComponentMapper<PositionComponent> positionMapper;
    
    @Inject
    ComponentMapper<VelocityComponent> velocityMapper;

    @Inject
    ComponentMapper<JumpComponent> jumpMapper;

    @Inject
    ComponentMapper<GameStageFinishedComponent> gameStageFinishedMapper;

    @Inject
    ComponentMapper<PlayerComponent> playerMapper;

    @Inject
    ComponentMapper<FloorComponent> floorMapper;

    @Inject
    ComponentMapper<TextureRegionComponent> textureRegionMapper;

    @Inject
    @Named("game-start-system.jump-velocity")
    float jumpVelocity;

    private Family floorFamily;

    @Inject
    public GameRunSystem(@Named("game-run-system.priority") int priority) {
        super(priority);
        floorFamily = Family.all(FloorComponent.class).get();
    }

    @Override
    public void update(float delta) {
        resetInvisibleFloor();
    }

    private void resetInvisibleFloor() {
        getEngine().getEntitiesFor(floorFamily).forEach(floor -> {
            var position = positionMapper.get(floor);
            if (position.getX() + WIDTH <= 0) {
                position.setX(WIDTH);
                textureRegionMapper.get(floor).setTextureRegion(getRandomFloorTextureRegion());
            }
        });
    }

    private TextureRegion getRandomFloorTextureRegion() {
        var floorTextures = new TextureRegion[] { assets.get(FLOOR1), assets.get(FLOOR2), assets.get(FLOOR3) };
        Collections.shuffle(Arrays.asList(floorTextures));
        return floorTextures[0];
    }
}