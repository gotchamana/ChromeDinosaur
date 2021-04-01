package chrome.dinosaur.ecs.system.gamestate;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.*;

import chrome.dinosaur.ecs.component.*;

public class GameStartSystem extends EntitySystem {
    
    @Inject
    ComponentMapper<PositionComponent> positionMapper;
    
    @Inject
    ComponentMapper<VelocityComponent> velocityMapper;

    @Inject
    ComponentMapper<JumpComponent> jumpMapper;

    @Inject
    ComponentMapper<GameStateFinishedComponent> gameStateFinishedMapper;

    @Inject
    ComponentMapper<PlayerComponent> playerMapper;

    @Inject
    @Named("game-start-system.jump-velocity")
    float jumpVelocity;

    private Family playerFamily;
    private Family gameStateFinishedFamily;
    
    @Inject
    public GameStartSystem(@Named("game-start-system.priority") int priority) {
        super(priority);
        playerFamily = Family.all(PositionComponent.class, VelocityComponent.class, PlayerComponent.class).get();
        gameStateFinishedFamily = Family.all(GameStateFinishedComponent.class).get();
    }
    
    @Override
    public void update(float delta) {
        var player = getEngine().getEntitiesFor(playerFamily).first();
        if (jumpMapper.has(player)) return;

        var playerComponent = playerMapper.get(player);

        if (playerComponent.isJumped()) {
            var gameStateFinished = getEngine().getEntitiesFor(gameStateFinishedFamily).first();
            gameStateFinishedMapper.get(gameStateFinished).setFinished(true);
            return;
        }

        if (isKeyUpOrSpacePressed()) {
            var position = positionMapper.get(player);
            var velocity = velocityMapper.get(player);

            player.add(new JumpComponent(position.getY(), () -> playerComponent.setJumped(true)));
            velocity.setY(jumpVelocity);
        }
    }

    private boolean isKeyUpOrSpacePressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }
}