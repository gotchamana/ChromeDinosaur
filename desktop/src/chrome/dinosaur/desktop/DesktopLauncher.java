package chrome.dinosaur.desktop;

import static chrome.dinosaur.ChromeDinosaur.*;

import com.badlogic.gdx.backends.lwjgl.*;

import chrome.dinosaur.ChromeDinosaur;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WIDTH;
		config.height = HEIGHT;

		new LwjglApplication(new ChromeDinosaur(), config);
	}
}
