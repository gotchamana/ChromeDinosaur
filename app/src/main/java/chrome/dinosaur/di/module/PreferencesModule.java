package chrome.dinosaur.di.module;

import javax.inject.Singleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {
    
    private PreferencesModule() {}

    @Provides
    @Singleton
    public static Preferences providePreferences() {
        var prefs = Gdx.app.getPreferences("chrome.dinosaur.settings");

        if (!prefs.contains("gravity")) prefs.putFloat("gravity", -1f);
        if (!prefs.contains("jump.velocity")) prefs.putFloat("jump.velocity", 20f);
        
        prefs.flush();

        return prefs;
    }
}