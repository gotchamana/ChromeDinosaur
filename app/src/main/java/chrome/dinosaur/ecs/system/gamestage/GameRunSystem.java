package chrome.dinosaur.ecs.system.gamestage;

import static chrome.dinosaur.ChromeDinosaur.WIDTH;
import static chrome.dinosaur.ChromeDinosaur.Asset.*;

import java.util.*;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.*;
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
    ComponentMapper<AnimationComponent> animationMapper;

    @Inject
    @Named("game-start-system.jump-velocity")
    float jumpVelocity;

    private ImmutableArray<Entity> floors;
    private Entity player;
    private Component playerAnimationComponent;
    private Component playerTextureRegionComponent;

    private GameState state = GameState.PREPARE_ENTITY;

    @Inject
    public GameRunSystem(@Named("game-run-system.priority") int priority) {
        super(priority);
    }

    @Override
    public void update(float delta) {
        if (state == GameState.PREPARE_ENTITY) {
            var floorFamily = Family.all(FloorComponent.class).get();
            floors = getEngine().getEntitiesFor(floorFamily);

            var playerFamily = Family.all(PlayerComponent.class).get();
            player = getEngine().getEntitiesFor(playerFamily).first();
            state = GameState.GAME_RUN;
        } else {
            handlePlayerJump();
        }

        resetInvisibleFloor();
    }

    private void handlePlayerJump() {
        if (isKeyUpOrSpacePressed() && !isPlayerJumping()) {
            var position = positionMapper.get(player);
            player.add(new JumpComponent(position.getY()));

            var velocity = velocityMapper.get(player);
            velocity.setY(jumpVelocity);

            playerTextureRegionComponent = player.addAndReturn(Objects.requireNonNullElse(playerTextureRegionComponent,
                new TextureRegionComponent(assets.get(JUMP_DINO))));
            playerAnimationComponent = Objects.requireNonNullElse(player.remove(AnimationComponent.class),
                playerAnimationComponent);
        }

        if (isPlayerJumped()) {
            player.add(Objects.requireNonNull(playerAnimationComponent, "Player's AnimationComponent wasn't set"));
            player.remove(TextureRegionComponent.class);
        }
    }

    private boolean isPlayerJumping() {
        return jumpMapper.has(player);
    }

    private boolean isPlayerJumped() {
        return !isPlayerJumping() && !animationMapper.has(player);
    }

    private void resetInvisibleFloor() {
        floors.forEach(floor -> {
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

    private boolean isKeyUpOrSpacePressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

    private enum GameState {
        PREPARE_ENTITY, GAME_RUN
    }
}