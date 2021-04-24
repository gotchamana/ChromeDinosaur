package chrome.dinosaur.di.module;

import javax.inject.Singleton;

import com.badlogic.ashley.core.ComponentMapper;

import chrome.dinosaur.ecs.component.*;
import dagger.Module;
import dagger.Provides;

@Module
public class ComponentMapperModule {
    
    private ComponentMapperModule() {}

    @Provides
    @Singleton
    public static ComponentMapper<PositionComponent> providePositionMapper() {
        return ComponentMapper.getFor(PositionComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<VelocityComponent> provideVelocityMapper() {
        return ComponentMapper.getFor(VelocityComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<TextureRegionComponent> provideTextureRegionMapper() {
        return ComponentMapper.getFor(TextureRegionComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<AnimationComponent> provideAnimationMapper() {
        return ComponentMapper.getFor(AnimationComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<JumpComponent> provideJumpMapper() {
        return ComponentMapper.getFor(JumpComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<GameStageFinishedComponent> provideGameStageFinishedMapper() {
        return ComponentMapper.getFor(GameStageFinishedComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<PlayerComponent> providePlayerMapper() {
        return ComponentMapper.getFor(PlayerComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<FloorComponent> provideFloorMapper() {
        return ComponentMapper.getFor(FloorComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<ObstacleComponent> provideObstacleMapper() {
        return ComponentMapper.getFor(ObstacleComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<ShapeComponent> provideShapeMapper() {
        return ComponentMapper.getFor(ShapeComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<ScoreComponent> provideScoreMapper() {
        return ComponentMapper.getFor(ScoreComponent.class);
    }
}