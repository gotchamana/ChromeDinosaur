package chrome.dinosaur.ecs.system;

import static chrome.dinosaur.ChromeDinosaur.Asset.*;
import static chrome.dinosaur.config.Config.DIGIT_WIDTH;

import java.util.*;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;

public class ScoreSystem extends EntitySystem {
    
    private static final List<Asset> DIGITS = List.of(ZERO_DIGIT, ONE_DIGIT, TWO_DIGIT, THREE_DIGIT, FOUR_DIGIT,
        FIVE_DIGIT, SIX_DIGIT, SEVEN_DIGIT, EIGHT_DIGIT, NINE_DIGIT);

    @Inject
    Sound scoreSound;

    @Inject
    ComponentMapper<ScoreComponent> scoreMapper;

    @Inject
    ComponentMapper<TextureRegionComponent> textureRegionMapper;

    private Map<Asset, TextureRegion> assets;
    private Pixmap scorePixmap;
    private Texture scoreTexture;

    private Family scoreFamily;
    private float aggregatedTime;

    private boolean blinking = false;
    private BlinkSetting blinkSetting = new BlinkSetting();

    @Inject
    public ScoreSystem(@Named("score-system.priority") int priority, Map<Asset, TextureRegion> assets) {
        super(priority);

        this.scoreFamily = Family.all(ScoreComponent.class).get();
        this.assets = assets;
        this.scorePixmap = new Pixmap(DIGIT_WIDTH * 14, assets.get(ZERO_DIGIT).getRegionHeight(),
            assets.get(ZERO_DIGIT).getTexture().getTextureData().getFormat());
        this.scoreTexture = new Texture(scorePixmap.getWidth(), scorePixmap.getHeight(), scorePixmap.getFormat());
    }

    @Override
    public void update(float deltaTime) {
        var scoreEntity = getEngine().getEntitiesFor(scoreFamily).first();
        var scoreComponent = scoreMapper.get(scoreEntity);
        var textureRegionComponent = Objects.requireNonNullElse(textureRegionMapper.get(scoreEntity),
            new TextureRegionComponent(new TextureRegion()));

        while (aggregatedTime >= 0.1) {
            scoreComponent.setCurrentScore((scoreComponent.getCurrentScore() + 1) % 100000);
            drawScore(scoreComponent.getHighScore(), scoreComponent.getCurrentScore());

            textureRegionComponent.getTextureRegion().setRegion(scoreTexture);
            scoreEntity.add(textureRegionComponent);

            aggregatedTime -= 0.1;
            blinkSetting.aggregatedTime += deltaTime;
        }

        aggregatedTime += deltaTime;
	}

    private void drawScore(int highScore, int currentScore) {
        if (currentScore > 0 && currentScore % 100 == 0) {
            blinking = true;
            scoreSound.play();
            resetBlinkSetting(currentScore);
        }

        scorePixmap.setColor(Color.CLEAR);
        scorePixmap.fill();

        var pixmap = getDigitsPixmap();
        final var scoreFormat = "%05d";

        if (highScore > 0)
            drawHighScore(pixmap, String.format(scoreFormat, highScore));

        if (blinking)
            blinkScore(pixmap, String.format(scoreFormat, blinkSetting.score));
        else
            drawCurrentScore(pixmap, String.format(scoreFormat, currentScore));

        pixmap.dispose();

        scoreTexture.draw(scorePixmap, 0, 0);
    }

    private void resetBlinkSetting(int currentScore) {
        blinkSetting.score = currentScore;
        blinkSetting.times = 0;
        blinkSetting.flag = true;
        blinkSetting.aggregatedTime = 0;
    }

    private void drawHighScore(Pixmap pixmap, String score) {
        var textureRegion = assets.get(HIGH_SCORE);
        scorePixmap.drawPixmap(pixmap, 0, 0, textureRegion.getRegionX(), textureRegion.getRegionY(),
            textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        drawScoreHelper(pixmap, 0, 3, score);
    }

    private void blinkScore(Pixmap pixmap, String score) {
        while (blinkSetting.aggregatedTime >= 0.05) {
            blinkSetting.flag = !blinkSetting.flag;
            blinkSetting.times++;
            blinkSetting.aggregatedTime -= 0.05;
        }

        if (blinkSetting.flag)
            drawCurrentScore(pixmap, score);
        
        if (blinkSetting.times >= 8)
            blinking = false;
    }

    private void drawCurrentScore(Pixmap pixmap, String score) {
        drawScoreHelper(pixmap, 0, 9, score);
    }

    private void drawScoreHelper(Pixmap pixmap, int index, int offset, String score) {
        if (index >= score.length()) return;

        var digit = score.charAt(index) - '0';
        var textureRegion = assets.get(DIGITS.get(digit));
        scorePixmap.drawPixmap(pixmap, DIGIT_WIDTH * (index + offset), 0, textureRegion.getRegionX(),
            textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        drawScoreHelper(pixmap, index + 1, offset, score);
    }

    private Pixmap getDigitsPixmap() {
        var data = assets.get(ZERO_DIGIT).getTexture().getTextureData();
        if (!data.isPrepared())
            data.prepare();

        return data.consumePixmap();
    }

    public void setScoreBlinking(boolean blinking) {
        this.blinking = blinking;

        if (!blinking) {
            var scoreEntity = getEngine().getEntitiesFor(scoreFamily).first();
            var scoreComponent = scoreMapper.get(scoreEntity);
            drawScore(scoreComponent.getHighScore(), scoreComponent.getCurrentScore());
        }
    }

    private static final class BlinkSetting {
        private boolean flag;
        private float aggregatedTime;
        private int times;
        private int score;
    }
}