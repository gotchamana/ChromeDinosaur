package chrome.dinosaur;

import javax.inject.Inject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chrome.dinosaur.component.DaggerGameComponent;
import chrome.dinosaur.component.GameComponent;

public class ChromeDinosaur extends Game {

	@Inject
	AssetManager assetManager;

	private GameComponent gameComponent;

	public ChromeDinosaur() {
		gameComponent = DaggerGameComponent.create();
		gameComponent.injectChromeDinosaur(this);
	}
	
	@Override
	public void create() {
		assetManager.load("sprite.atlas", TextureAtlas.class);
		assetManager.finishLoading();

		screen = new GameStart();
		gameComponent.injectGameStart((GameStart) screen);
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose () {
		screen.dispose();
		assetManager.dispose();
	}
}
