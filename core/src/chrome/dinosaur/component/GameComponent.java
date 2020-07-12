package chrome.dinosaur.component;

import javax.inject.Singleton;

import chrome.dinosaur.ChromeDinosaur;
import chrome.dinosaur.GameStart;
import chrome.dinosaur.module.AssetModule;
import dagger.Component;

@Singleton
@Component(modules = AssetModule.class)
public interface GameComponent {
    public void injectChromeDinosaur(ChromeDinosaur game);
    public void injectGameStart(GameStart screen);
}