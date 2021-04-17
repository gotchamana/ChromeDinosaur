package chrome.dinosaur.ecs.system;

import javax.inject.*;

import com.badlogic.ashley.core.*;

import chrome.dinosaur.ecs.component.GameStageFinishedComponent;

public class GameStageFinishedSystem extends EntitySystem {
    
    @Inject
    ComponentMapper<GameStageFinishedComponent> gameStageFinishedMapper;
    
    private Family gameStageFinishedFamily;
    
    @Inject
    public GameStageFinishedSystem(@Named("game-state-finished-system.priority") int priority) {
        super(priority);
        gameStageFinishedFamily = Family.all(GameStageFinishedComponent.class).get();
    }

    @Override
    public void update(float delta) {
        var gameStageFinished = getEngine().getEntitiesFor(gameStageFinishedFamily).first();
        var gameStageFinishedComponent = gameStageFinishedMapper.get(gameStageFinished);

        if (gameStageFinishedComponent.isFinished())
            gameStageFinishedComponent.getOnFinished().run();
    }
}