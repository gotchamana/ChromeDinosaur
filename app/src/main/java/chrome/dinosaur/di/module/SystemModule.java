package chrome.dinosaur.di.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class SystemModule {

    private SystemModule() {}

    @Provides
    @Named("movement-system.priority")
    public static int provideMovementSystemPriority() {
        return 0;
    }

    @Provides
    @Named("game-start-system.priority")
    public static int provideGameStartSystemPriority() {
        return 1;
    }

    @Provides
    @Named("render-system.priority")
    public static int provideRenderSystemPriority() {
        return 2;
    }

    @Provides
    @Named("score-system.priority")
    public static int provideScoreSystemPriority() {
        return 1;
    }

    @Provides
    @Named("game-state-finished-system.priority")
    public static int provideGameStateFinishedSystemPriority() {
        return 3;
    }

    @Provides
    @Named("game-run-system.priority")
    public static int provideGameRunSystemPriority() {
        return 1;
    }

    @Provides
    @Named("game-over-system.priority")
    public static int provideGameOverSystemPriority() {
        return 1;
    }
}