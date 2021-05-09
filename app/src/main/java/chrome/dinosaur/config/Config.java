package chrome.dinosaur.config;

import java.util.List;

public class Config {
    
    public static final int WIDTH = 800;
    public static final int HEIGHT = 300;
    public static final int DIGIT_WIDTH = 20;

    public static final String TEXTURE_ATLAS_NAME = "sprite.atlas";
    public static final String SCORE_SOUND_NAME = "score.ogg";

    public static final float GRAVITY = -1f;
    public static final float JUMP_VELOCITY = 20f;
    public static final float INITIAL_VELOCITY = -10f;
    public static final float CLOUD_VELOCITY = -5f;

    public static final int CLOUD_MIN_SPACE = 150;
    public static final int CLOUD_MAX_SPACE = WIDTH - 100;

    public static final List<Float> CLOUD_Y = List.of(110f, 130f, 150f, 170f, 190f);

    private Config() {}
}