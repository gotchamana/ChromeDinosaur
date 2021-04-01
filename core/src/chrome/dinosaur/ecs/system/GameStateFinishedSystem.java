package chrome.dinosaur.ecs.system;

import javax.inject.*;

import com.badlogic.ashley.core.*;

import chrome.dinosaur.ecs.component.GameStateFinishedComponent;

public class GameStateFinishedSystem extends EntitySystem {
    
    @Inject
    ComponentMapper<GameStateFinishedComponent> gameStateFinishedMapper;
    
    private Family gameStateFinishedFamily;
    
    @Inject
    public GameStateFinishedSystem(@Named("game-state-finished-system.priority") int priority) {
        super(priority);
        gameStateFinishedFamily = Family.all(GameStateFinishedComponent.class).get();
    }

    @Override
    public void update(float delta) {
        Entity gameStateFinished = getEngine().getEntitiesFor(gameStateFinishedFamily).first();
        GameStateFinishedComponent gameStateFinishedComponent = gameStateFinishedMapper.get(gameStateFinished);

        if (gameStateFinishedComponent.isFinished())
            gameStateFinishedComponent.getOnFinished().run();
    }
}