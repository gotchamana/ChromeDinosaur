package chrome.dinosaur.gamestate;

import com.badlogic.gdx.ScreenAdapter;

public abstract class GameStage extends ScreenAdapter {
    
    protected Runnable onFinished;
    
    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }
}