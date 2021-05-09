package chrome.dinosaur.ecs.system.gamestage;

import static chrome.dinosaur.ChromeDinosaur.Asset.SHOCK_DINO;
import static chrome.dinosaur.config.Config.HEIGHT;

import java.util.Map;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GameOverSystem extends EntitySystem {
    
    @Inject
    Preferences preferences;

    @Inject
    Map<Asset, TextureRegion> assets;

    @Inject
    ComponentMapper<AnimationComponent> animationMapper;

    @Inject
    ComponentMapper<PositionComponent> positionMapper;

    @Inject
    ComponentMapper<TextureRegionComponent> textureRegionMapper;

    @Inject
    ComponentMapper<GameStageFinishedComponent> gameStageFinishedMapper;

    @Inject
    ComponentMapper<ScoreComponent> scoreMapper;

    private Entity restartButton;
    private Entity gameStageFinished;

    private GameState state = GameState.INIT_STAGE;

    @Inject
    public GameOverSystem(@Named("game-over-system.priority") int priority) {
        super(priority);
    }

    @Override
    public void update(float delta) {
        if (state == GameState.INIT_STAGE) {
            var playerFamily = Family.all(PlayerComponent.class).get();
            var player = getEngine().getEntitiesFor(playerFamily).first();
            player.add(new TextureRegionComponent(assets.get(SHOCK_DINO)));

            var restartBtnFamily = Family.all(RestartButtonComponent.class).get();
            restartButton = getEngine().getEntitiesFor(restartBtnFamily).first();

            var gameSageFinishedFamily = Family.all(GameStageFinishedComponent.class).get();
            gameStageFinished = getEngine().getEntitiesFor(gameSageFinishedFamily).first();

            var scoreFamily = Family.all(ScoreComponent.class).get();
            var score = getEngine().getEntitiesFor(scoreFamily).first();
            var scoreComponent = scoreMapper.get(score);
            updateHighScore(scoreComponent);
            scoreComponent.setCurrentScore(0);

            state = GameState.GAME_RESTART;
        } else if (isRestartButtonClicked()) {
            log.debug("Restart button was clicked");
            state = GameState.INIT_STAGE;
            gameStageFinishedMapper.get(gameStageFinished).setFinished(true);
        }
    }

    private void updateHighScore(ScoreComponent scoreComponent) {
        if (scoreComponent.getCurrentScore() > preferences.getInteger("high.score", 0)) {
            scoreComponent.setHighScore(scoreComponent.getCurrentScore());
            preferences.putInteger("high.score", scoreComponent.getCurrentScore());
            preferences.flush();
        }
    }

    private boolean isRestartButtonClicked() {
        var mouseX = Gdx.input.getX();
        var mouseY = HEIGHT - Gdx.input.getY();

        var position = positionMapper.get(restartButton);
        var textureRegion = textureRegionMapper.get(restartButton).getTextureRegion();

        var buttonX = position.getX();
        var buttonY = position.getY();
        var buttonWidth = textureRegion.getRegionWidth();
        var buttonHeight = textureRegion.getRegionHeight();

        return Gdx.input.isButtonJustPressed(Buttons.LEFT) && buttonX <= mouseX && buttonX + buttonWidth >= mouseX
            && buttonY <= mouseY && buttonY + buttonHeight >= mouseY;
    }

    private enum GameState {
        INIT_STAGE, GAME_RESTART
    }
}