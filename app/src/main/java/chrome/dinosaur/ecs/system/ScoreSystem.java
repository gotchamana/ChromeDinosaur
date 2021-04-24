package chrome.dinosaur.ecs.system;

import static chrome.dinosaur.ChromeDinosaur.DIGIT_WIDTH;
import static chrome.dinosaur.ChromeDinosaur.Asset.*;

import java.util.*;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import chrome.dinosaur.ChromeDinosaur.Asset;
import chrome.dinosaur.ecs.component.*;

public class ScoreSystem extends EntitySystem {
    
    private static final List<Asset> DIGITS = List.of(ZERO_DIGIT, ONE_DIGIT, TWO_DIGIT, THREE_DIGIT, FOUR_DIGIT,
        FIVE_DIGIT, SIX_DIGIT, SEVEN_DIGIT, EIGHT_DIGIT, NINE_DIGIT);

    @Inject
    ComponentMapper<ScoreComponent> scoreMapper;

    @Inject
    ComponentMapper<TextureRegionComponent> textureRegionMapper;

    private Map<Asset, TextureRegion> assets;
    private Pixmap scorePixmap;
    private Texture scoreTexture;

    private Family scoreFamily;
    private float elapsedTime;

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

        while (elapsedTime >= 0.1) {
            scoreComponent.setCurrentScore((scoreComponent.getCurrentScore() + 1) % 100000);
            drawScore(scoreComponent.getHighScore(), scoreComponent.getCurrentScore());

            textureRegionComponent.getTextureRegion().setRegion(scoreTexture);
            scoreEntity.add(textureRegionComponent);

            elapsedTime -= 0.1;
        }

        elapsedTime += deltaTime;
	}

    private void drawScore(int highScore, int currentScore) {
        scorePixmap.setColor(Color.CLEAR);
        scorePixmap.fill();

        var pixmap = getDigitsPixmap();

        final var format = "%05d";
        if (highScore > 0)
            drawHighScore(pixmap, String.format(format, highScore));
        drawCurrentScore(pixmap, String.format(format, currentScore));

        pixmap.dispose();

        scoreTexture.draw(scorePixmap, 0, 0);
    }

    private void drawHighScore(Pixmap pixmap, String score) {
        var textureRegion = assets.get(HIGH_SCORE);
        scorePixmap.drawPixmap(pixmap, 0, 0, textureRegion.getRegionX(), textureRegion.getRegionY(),
            textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        drawScoreHelper(pixmap, 0, 3, score);
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
}