package chrome.dinosaur.di.module;

import javax.inject.Singleton;

import com.badlogic.ashley.core.Engine;

import chrome.dinosaur.ecs.system.*;
import chrome.dinosaur.ecs.system.gamestage.*;
import dagger.Module;
import dagger.Provides;

@Module
public class EngineModule {

    private EngineModule() {}

    @Provides
    @Singleton
    public static Engine provideEngine(MovementSystem movementSystem, RenderSystem renderSystem,
        GameStartSystem gameStartSystem, GameRunSystem gameRunSystem,
        GameStageFinishedSystem gameStageFinishedSystem) {

        gameRunSystem.setProcessing(false);

        var engine = new Engine();
        engine.addSystem(movementSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(gameStageFinishedSystem);
        engine.addSystem(gameStartSystem);
        engine.addSystem(gameRunSystem);

        return engine;
    }
}