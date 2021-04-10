package chrome.dinosaur.ecs.system.gamestage;

import static chrome.dinosaur.ChromeDinosaur.WIDTH;
import static chrome.dinosaur.ChromeDinosaur.Asset.*;

import java.util.*;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

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
    ComponentMapper<ObstacleComponent> obstacleMapper;

    @Inject
    ComponentMapper<TextureRegionComponent> textureRegionMapper;

    @Inject
    ComponentMapper<AnimationComponent> animationMapper;

    @Inject
    @Named("game-start-system.jump-velocity")
    float jumpVelocity;

    private ImmutableArray<Entity> floors;
    private ImmutableArray<Entity> obstacles;
    private Entity player;
    private Component playerWalkAnimationComponent;
    private Component playerCrouchWalkAnimationComponent;
    private Component playerJumpTextureRegionComponent;

    private GameState state = GameState.INIT_STAGE;

    @Inject
    public GameRunSystem(@Named("game-run-system.priority") int priority) {
        super(priority);
    }

    @Override
    public void update(float delta) {
        if (state == GameState.INIT_STAGE) {
            var floorFamily = Family.all(FloorComponent.class).get();
            floors = getEngine().getEntitiesFor(floorFamily);

            var obstacleFamily = Family.all(ObstacleComponent.class).get();
            obstacles = getEngine().getEntitiesFor(obstacleFamily);

            var playerFamily = Family.all(PlayerComponent.class).get();
            player = getEngine().getEntitiesFor(playerFamily).first();

            var animation = new Animation<>(0.1f, assets.get(CROUCH_WALK_DINO1), assets.get(CROUCH_WALK_DINO2));
            animation.setPlayMode(PlayMode.LOOP);
            playerCrouchWalkAnimationComponent = new AnimationComponent(animation);

            playerWalkAnimationComponent = Objects.requireNonNull(animationMapper.get(player),
                "Player doesn't have AnimationComponent");
            playerJumpTextureRegionComponent = new TextureRegionComponent(assets.get(JUMP_DINO));

            state = GameState.GAME_RUN;
        } else {
            handlePlayerJump();
            handlePlayerCrouch();
        }

        resetInvisibleFloor();
        resetInvisibleObstacle();
    }

    private void handlePlayerJump() {
        if (isKeyUpOrSpacePressed() && !isPlayerJumping()) {
            var position = positionMapper.get(player);
            player.add(new JumpComponent(position.getY()));

            var velocity = velocityMapper.get(player);
            velocity.setY(jumpVelocity);

            player.add(playerJumpTextureRegionComponent);
            player.remove(AnimationComponent.class);
        }

        if (isPlayerJumped()) {
            player.add(playerWalkAnimationComponent);
            player.remove(TextureRegionComponent.class);
        }
    }

    private boolean isKeyUpOrSpacePressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

    private boolean isPlayerJumping() {
        return jumpMapper.has(player);
    }

    private boolean isPlayerJumped() {
        return !isPlayerJumping() && !animationMapper.has(player);
    }

    private void handlePlayerCrouch() {
        if (isKeyDownPressed() && !isPlayerJumping())
            player.add(playerCrouchWalkAnimationComponent);
        else if (!isKeyDownPressed() && !isPlayerJumping())
            player.add(playerWalkAnimationComponent);
    }

    private boolean isKeyDownPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.DOWN);
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

    private void resetInvisibleObstacle() {
        obstacles.forEach(obstacle -> {
            var position = positionMapper.get(obstacle);
            var textureRegionComponent = textureRegionMapper.get(obstacle);
            if (position.getX() + textureRegionComponent.getTextureRegion().getRegionWidth() <= 0) {
                position.setX(WIDTH * 2f);
                textureRegionComponent.setTextureRegion(getRandomObstacleTextureRegion());
            }
        });
    }

    private TextureRegion getRandomObstacleTextureRegion() {
        var obstacleTextures = new TextureRegion[] { assets.get(SMALL_CACTUS_ONE), assets.get(SMALL_CACTUS_TWO),
            assets.get(SMALL_CACTUS_THREE), assets.get(LARGE_CACTUS_ONE), assets.get(LARGE_CACTUS_TWO),
            assets.get(LARGE_CACTUS_FOUR) };
        Collections.shuffle(Arrays.asList(obstacleTextures));
        return obstacleTextures[0];
    }

    private enum GameState {
        INIT_STAGE, GAME_RUN
    }
}