package chrome.dinosaur.di.module;

import javax.inject.Singleton;

import com.badlogic.ashley.core.Engine;

import chrome.dinosaur.ecs.system.*;
import chrome.dinosaur.ecs.system.gamestate.GameStartSystem;
import dagger.Module;
import dagger.Provides;

@Module
public class EngineModule {

    private EngineModule() {}

    @Provides
    @Singleton
    public static Engine provideEngine(MovementSystem movementSystem, RenderSystem renderSystem,
        GameStartSystem gameStartSystem, GameStageFinishedSystem gameStageFinishedSystem) {

        var engine = new Engine();
        engine.addSystem(movementSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(gameStartSystem);
        engine.addSystem(gameStageFinishedSystem);

        return engine;
    }
}