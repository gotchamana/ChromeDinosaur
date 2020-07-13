package chrome.dinosaur.di.component;

import javax.inject.Singleton;

import chrome.dinosaur.ChromeDinosaur;
import chrome.dinosaur.di.module.AssetModule;
import chrome.dinosaur.di.module.GraphicsModule;
import chrome.dinosaur.gamestate.GameStart;
import dagger.Component;

@Singleton
@Component(modules = { AssetModule.class, GraphicsModule.class })
public interface GameComponent {
    public void injectChromeDinosaur(ChromeDinosaur game);
    public GameStart injectGameStart(GameStart screen);
}