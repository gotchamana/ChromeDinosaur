package chrome.dinosaur.ecs.system.gamestage;

import static chrome.dinosaur.ChromeDinosaur.WIDTH;
import static chrome.dinosaur.ChromeDinosaur.Asset.*;

import java.util.Map;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;

public class GameStartSystem extends EntitySystem {
    
    @Inject
    Preferences preferences;

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
    ComponentMapper<ScoreComponent> scoreMapper;

    @Inject
    @Named("game-start-system.jump-velocity")
    float jumpVelocity;

    private Entity player;
    private Entity whiteBlock;
    private Entity gameStateFinished;

    private GameState state = GameState.PREPARE_ENTITY;
    
    @Inject
    public GameStartSystem(@Named("game-start-system.priority") int priority) {
        super(priority);
    }
    
    @Override
    public void update(float delta) {
        switch (state) {
            case PREPARE_ENTITY:
                var playerFamily = Family.all(PositionComponent.class, VelocityComponent.class, PlayerComponent.class)
                    .get();
                var whiteBlockFamily = Family.all(PositionComponent.class, VelocityComponent.class)
                    .exclude(PlayerComponent.class).get();
                var gameStateFinishedFamily = Family.all(GameStageFinishedComponent.class).get();

                player = getEngine().getEntitiesFor(playerFamily).first();
                whiteBlock = getEngine().getEntitiesFor(whiteBlockFamily).first();
                gameStateFinished = getEngine().getEntitiesFor(gameStateFinishedFamily).first();

                state = GameState.WAIT_PLAYER_JUMP;
                break;

            case WAIT_PLAYER_JUMP:
                if (isKeyUpOrSpacePressed() && !jumpMapper.has(player)) {
                    var position = positionMapper.get(player);
                    var velocity = velocityMapper.get(player);

                    player.add(new JumpComponent(position.getY()));
                    velocity.setY(jumpVelocity);
                    state = GameState.PLAYER_JUMPED;
                }
                break;
        
            case PLAYER_JUMPED:
                if (!jumpMapper.has(player))
                    state = GameState.PREPARE_GAME_SCENE;
                break;

            case PREPARE_GAME_SCENE:
                var animation = new Animation<>(0.1f, assets.get(WALK_DINO1), assets.get(WALK_DINO2));
                animation.setPlayMode(PlayMode.LOOP);
                player.add(new AnimationComponent(animation));
                velocityMapper.get(player).setX(1);
                player.remove(TextureRegionComponent.class);

                var score = getEngine().getEntitiesFor(Family.all(ScoreComponent.class).get()).first();
                var scoreComponent = scoreMapper.get(score);
                scoreComponent.setHighScore(preferences.getInteger("high.score", 0));
                scoreComponent.setCurrentScore(0);

                whiteBlock.getComponent(VelocityComponent.class).setX(20);
                state = GameState.GAME_READY;
                break;

            case GAME_READY:
                if (positionMapper.get(whiteBlock).getX() >= WIDTH) {
                    velocityMapper.get(player).setX(0);
                    gameStageFinishedMapper.get(gameStateFinished).setFinished(true);
                }
                break;
        }
    }

    private boolean isKeyUpOrSpacePressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

    private enum GameState {
        PREPARE_ENTITY, WAIT_PLAYER_JUMP, PLAYER_JUMPED, PREPARE_GAME_SCENE, GAME_READY
    }
}