package com.connectike.energycrisis;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

	// Usage: someTexture = assets.manager.get(Assets.raiderSprite);
	public static void load() {		
		manager.load(raiderSprite);
		manager.load(banditSprite);
		manager.load(playerSprite);
		manager.load(newwaveSound);
		manager.load(mainmusic);
		manager.load(generatorfuel);
		manager.load(playerattack);
		manager.load(startsound);
		
		manager.finishLoading();
	}
	
	public static void dispose() {
		manager.dispose();
	}

	public static AssetManager manager = new AssetManager();
	
    public static final AssetDescriptor<Texture> raiderSprite = 
            new AssetDescriptor<Texture>("spritesheets/raider.png", Texture.class);
    
    public static final AssetDescriptor<Texture> banditSprite = 
            new AssetDescriptor<Texture>("spritesheets/bandit.png", Texture.class);
    
    public static final AssetDescriptor<Texture> playerSprite = 
            new AssetDescriptor<Texture>("spritesheets/player.png", Texture.class);
    
    public static final AssetDescriptor<Sound> newwaveSound = 
            new AssetDescriptor<Sound>("audio/newwave.mp3", Sound.class);
    
    public static final AssetDescriptor<Music> mainmusic = 
            new AssetDescriptor<Music>("audio/mainmusic.mp3", Music.class);
    
    public static final AssetDescriptor<Sound> generatorfuel = 
            new AssetDescriptor<Sound>("audio/generatorfuel.mp3", Sound.class);
    
    public static final AssetDescriptor<Sound> playerattack = 
            new AssetDescriptor<Sound>("audio/playerattack.mp3", Sound.class);
    
    public static final AssetDescriptor<Sound> startsound = 
            new AssetDescriptor<Sound>("audio/startsound.mp3", Sound.class);
}
