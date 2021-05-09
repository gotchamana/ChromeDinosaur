package chrome.dinosaur.di.module;

import javax.inject.Singleton;

import com.badlogic.gdx.*;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {
    
    private PreferencesModule() {}

    @Provides
    @Singleton
    public static Preferences providePreferences() {
        return Gdx.app.getPreferences("chrome.dinosaur.settings");
    }
}