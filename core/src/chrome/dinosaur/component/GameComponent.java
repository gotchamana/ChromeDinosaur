package chrome.dinosaur.component;

import javax.inject.Singleton;

import chrome.dinosaur.ChromeDinosaur;
import chrome.dinosaur.GameStart;
import chrome.dinosaur.module.AssetModule;
import chrome.dinosaur.module.GraphicsModule;
import dagger.Component;

@Singleton
@Component(modules = { AssetModule.class, GraphicsModule.class })
public interface GameComponent {
    public void injectChromeDinosaur(ChromeDinosaur game);
    public GameStart injectGameStart(GameStart screen);
}