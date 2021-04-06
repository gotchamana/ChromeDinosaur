package chrome.dinosaur.di.component;

import javax.inject.Singleton;

import chrome.dinosaur.ChromeDinosaur;
import chrome.dinosaur.di.module.*;
import dagger.Component;

@Singleton
@Component(modules = {
    PreferencesModule.class, AssetModule.class,
    GraphicsModule.class, EngineModule.class,
    ComponentMapperModule.class, SystemModule.class
})
public interface GameComponent {
    void chromeDinosaur(ChromeDinosaur chromeDinosaur);
}