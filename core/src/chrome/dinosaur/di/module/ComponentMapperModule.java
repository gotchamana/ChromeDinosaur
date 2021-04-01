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
    public static ComponentMapper<GameStateFinishedComponent> provideGameStateFinishedMapper() {
        return ComponentMapper.getFor(GameStateFinishedComponent.class);
    }

    @Provides
    @Singleton
    public static ComponentMapper<PlayerComponent> providePlayerMapper() {
        return ComponentMapper.getFor(PlayerComponent.class);
    }
}