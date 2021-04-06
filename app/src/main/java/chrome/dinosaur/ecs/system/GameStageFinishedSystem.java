package chrome.dinosaur.ecs.system;

import javax.inject.*;

import com.badlogic.ashley.core.*;

import chrome.dinosaur.ecs.component.GameStageFinishedComponent;

public class GameStageFinishedSystem extends EntitySystem {
    
    @Inject
    ComponentMapper<GameStageFinishedComponent> gameStageFinishedMapper;
    
    private Family gameStateFinishedFamily;
    
    @Inject
    public GameStageFinishedSystem(@Named("game-state-finished-system.priority") int priority) {
        super(priority);
        gameStateFinishedFamily = Family.all(GameStageFinishedComponent.class).get();
    }

    @Override
    public void update(float delta) {
        var gameStateFinished = getEngine().getEntitiesFor(gameStateFinishedFamily).first();
        var gameStageFinishedComponent = gameStageFinishedMapper.get(gameStateFinished);

        if (gameStageFinishedComponent.isFinished())
            gameStageFinishedComponent.getOnFinished().run();
    }
}