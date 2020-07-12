package chrome.dinosaur;

import javax.inject.Inject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chrome.dinosaur.component.DaggerGameComponent;
import chrome.dinosaur.component.GameComponent;
import dagger.Lazy;

public class ChromeDinosaur extends Game {

	@Inject
	AssetManager assetManager;

	@Inject
	Lazy<SpriteBatch> lazyBatch;
	
	private GameComponent gameComponent;

	public ChromeDinosaur() {
		gameComponent = DaggerGameComponent.create();
		gameComponent.injectChromeDinosaur(this);
	}
	
	@Override
	public void create() {
		assetManager.load("sprite.atlas", TextureAtlas.class);
		assetManager.finishLoading();

		setScreen(gameComponent.injectGameStart(new GameStart()));
	}

	@Override
	public void dispose() {
		screen.dispose();
		assetManager.dispose();
		lazyBatch.get().dispose();
	}
}
