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
import com.badlogic.gdx.math.*;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
    ComponentMapper<ShapeComponent> shapeMapper;

    @Inject
    @Named("game-start-system.jump-velocity")
    float jumpVelocity;

    private ImmutableArray<Entity> floors;
    private ImmutableArray<Entity> obstacles;

    private Entity player;
    private Entity gameStageFinished;

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
        switch (state) {
            case INIT_STAGE:
                var floorFamily = Family.all(FloorComponent.class).get();
                floors = getEngine().getEntitiesFor(floorFamily);

                var obstacleFamily = Family.all(ObstacleComponent.class).get();
                obstacles = getEngine().getEntitiesFor(obstacleFamily);

                var gameStageFinishedFamily = Family.all(GameStageFinishedComponent.class).get();
                gameStageFinished = getEngine().getEntitiesFor(gameStageFinishedFamily).first();

                var playerFamily = Family.all(PlayerComponent.class).get();
                player = getEngine().getEntitiesFor(playerFamily).first();

                positionMapper.get(player).setY(0);

                playerCrouchWalkAnimationComponent = Objects.requireNonNullElseGet(playerCrouchWalkAnimationComponent,
                    () -> {
                        var animation = new Animation<>(0.1f, assets.get(CROUCH_WALK_DINO1),
                            assets.get(CROUCH_WALK_DINO2));
                        animation.setPlayMode(PlayMode.LOOP);
                        return new AnimationComponent(animation);
                    });

                playerWalkAnimationComponent = Objects.requireNonNullElse(playerWalkAnimationComponent,
                    animationMapper.get(player));

                playerJumpTextureRegionComponent = Objects.requireNonNullElse(playerJumpTextureRegionComponent,
                    new TextureRegionComponent(assets.get(JUMP_DINO)));

                var velocityFamily = Family.all(VelocityComponent.class).exclude(PlayerComponent.class).get();
                getEngine().getEntitiesFor(velocityFamily).forEach(entity -> velocityMapper.get(entity).setX(-10));

                state = GameState.GAME_RUN;
                break;
        
            case GAME_RUN:
                handlePlayerJump();
                handlePlayerCrouch();
                handlePlayerCollision();
                break;

            case GAME_OVER:
                velocityMapper.get(player).setY(0);
                player.remove(JumpComponent.class);
                player.remove(AnimationComponent.class);

                velocityFamily = Family.all(VelocityComponent.class).exclude(PlayerComponent.class).get();
                getEngine().getEntitiesFor(velocityFamily).forEach(entity -> velocityMapper.get(entity).setX(0));

                state = GameState.INIT_STAGE;
                gameStageFinishedMapper.get(gameStageFinished).setFinished(true);
                break;
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

    private void handlePlayerCollision() {
        var playerShapeComponent = shapeMapper.get(player);
        obstacles.forEach(obstacle -> {
            var playerShape = playerShapeComponent.getShape();
            var obstacleShape = shapeMapper.get(obstacle).getShape();
            if (Intersector.overlapConvexPolygons(playerShape, obstacleShape)) {
                log.debug("Player was collided with obstacle, player's polygon vertices: {}, obstacle's polygon vertices: {}",
                    playerShape.getTransformedVertices(), obstacleShape.getTransformedVertices());
                velocityMapper.get(player).setY(0);
                state = GameState.GAME_OVER;
            }
        });
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
            if (!isObstacleInvisible(position, textureRegionComponent)) return;
            
            position.setX(WIDTH * 2f);

            var randomObstacle = getRandomObstacleAsset();
            textureRegionComponent.setTextureRegion(assets.get(randomObstacle));

            var obstacleShape = randomObstacle.getShape();
            obstacleShape.setPosition(position.getX(), position.getY());
            shapeMapper.get(obstacle).setShape(obstacleShape);
        });
    }

    private boolean isObstacleInvisible(PositionComponent position, TextureRegionComponent textureRegionComponent) {
        return position.getX() + textureRegionComponent.getTextureRegion().getRegionWidth() <= 0;
    }

    private Asset getRandomObstacleAsset() {
        var obstacleAssets = new Asset[] { SMALL_CACTUS_ONE, SMALL_CACTUS_TWO, SMALL_CACTUS_THREE, LARGE_CACTUS_ONE,
            LARGE_CACTUS_TWO, LARGE_CACTUS_FOUR };
        return obstacleAssets[MathUtils.random.nextInt(obstacleAssets.length)];
    }

    private enum GameState {
        INIT_STAGE, GAME_RUN, GAME_OVER
    }
}