package com.connectike.energycrisis.screens;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.connectike.energycrisis.Assets;
import com.connectike.energycrisis.EnergyCrisisGame;
import com.connectike.energycrisis.PlayScreenInputHandler;
import com.connectike.energycrisis.creatures.Creature.State;
import com.connectike.energycrisis.creatures.Enemy;
import com.connectike.energycrisis.creatures.Player;
import com.connectike.energycrisis.creatures.Spawner;
import com.connectike.energycrisis.tools.Constants;
import com.connectike.energycrisis.tools.WorldContactListener;
import com.connectike.energycrisis.tools.WorldCreator;
import com.connectike.energycrisis.ui.HUD;

public class PlayScreen implements Screen {

	Sound newwave;
	Sound generatorfuel;
	Music mainMusic;
	public static Sound playerAttack;
	Sound startSound;
	
	private int musicStartCount;
	private boolean isMusicStarted;

	public static float waveCount = 0.85f; // Countdown to the next wave
	public static int currentWave = 0;
	public static int enemyCount = 0;
	public static boolean isWaveSpawning = false;
	public static boolean waveDefeated = true;

	private int enemyWaveCounter = 0;

	private EnergyCrisisGame game;

	private OrthographicCamera cam;
	private Viewport view;

	public Spawner spawner;

	OrthogonalTiledMapRenderer renderer;
	TmxMapLoader mapLoader;

	private PlayScreenInputHandler inputHandler;

	// map and worlds
	World world;
	private WorldCreator worldCreator;
	private TiledMap map;
	private Box2DDebugRenderer b2dr;
	private final String MAP_PATH = "maps/open_map.tmx";

	private Player p;
	FPSLogger logger;
	public static ArrayList<Enemy> enemyList = new ArrayList<Enemy>();

	private HUD hud;

	private float mapWidth, mapHeight = 0.0f;
	private float gameTime = 0.0f; // Total time of game running
	private boolean isGameOverSchedualed;
	public boolean shouldReset = false;

	public PlayScreen(EnergyCrisisGame game) {

		this.game = game;

		Assets.load();
		
		newwave = Assets.manager.get(Assets.newwaveSound);
		generatorfuel = Assets.manager.get(Assets.generatorfuel);
		mainMusic = Assets.manager.get(Assets.mainmusic);
		playerAttack = Assets.manager.get(Assets.playerattack);
		startSound = Assets.manager.get(Assets.startsound);

		System.out.println("creating world...");
		world = new World(new Vector2(0, 0), true);

		logger = new FPSLogger();

		System.out.println("setting camera...");
		// set a camera and bind a viewport to it
		cam = new OrthographicCamera();
		view = new ScreenViewport(cam);

		System.out.println("loading maps....");
		// load our map with the TmxMapLoader
		mapLoader = new TmxMapLoader();
		map = mapLoader.load(MAP_PATH);
		worldCreator = new WorldCreator(map, world);

		renderer = new OrthogonalTiledMapRenderer(map);

		System.out.println("loading player....");
		p = new Player(world);
		p.setLocation(Constants.PLAYER_SPAWN);

		hud = new HUD(game.batch);

		System.out.println("finalizing...");
		inputHandler = new PlayScreenInputHandler(p);
		Gdx.input.setInputProcessor(inputHandler);
		
		world.setContactListener(new WorldContactListener(map, p));
		b2dr = new Box2DDebugRenderer();
		
		spawner = new Spawner(world, p, enemyList, view);
		
		// get the width and height of the map (in tile count)
		mapWidth = Float.valueOf((Integer) map.getProperties().get("width"));
		mapHeight = Float.valueOf((Integer) map.getProperties().get("height"));
		// multiply by tile size (ex. 32x32)
		cam.translate(mapWidth / 2 * 32, mapHeight / 2 * 32);
		
//		spawner.spawnHorde();
//		Gdx.app.log("Wave", currentWave + " has begun");

		
		long start = startSound.play();
		startSound.setVolume(start, 1.0f);
	}

	public void handleInput() {

		if (inputHandler.isSpace && !p.isAttackBuffering()) {
			p.setAttacking(true);
			p.setAttackBuffering(true);
			p.setAnimationLooping(true);
			p.body.setLinearVelocity(new Vector2(0.0f, 0.0f));
		}

		// movement
		if (inputHandler.isW && !p.isAttacking()) {
			p.currentState = Player.State.UP;
			p.body.applyLinearImpulse(new Vector2(0.0f, p.getSpd()), p.body.getWorldCenter(), true);
		}
		if (inputHandler.isA && !p.isAttacking()) {
			p.currentState = Player.State.LEFT;
			p.body.applyLinearImpulse(new Vector2(-p.getSpd(), 0.0f), p.body.getWorldCenter(), true);
		}
		if (inputHandler.isS && !p.isAttacking()) {
			p.currentState = Player.State.DOWN;
			p.body.applyLinearImpulse(new Vector2(0.0f, -p.getSpd()), p.body.getWorldCenter(), true);
		}
		if (inputHandler.isD && !p.isAttacking()) {
			p.currentState = Player.State.RIGHT;
			p.body.applyLinearImpulse(new Vector2(p.getSpd(), 0.0f), p.body.getWorldCenter(), true);
		}
		if ((!inputHandler.isW) && (!inputHandler.isA) && (!inputHandler.isS) && (!inputHandler.isD)
				&& (!p.isAttacking())) {
			p.body.setLinearVelocity(new Vector2(0.0f, 0.0f));
			p.setAnimationLooping(false);
			p.elapsedTime = 0.0f;
		}
		
		// TODO: Disable zoom before ship!
		if (inputHandler.scrollUp) {
			cam.zoom += .05;
			inputHandler.scrollUp = false;
		}
		if (inputHandler.scrollDown) {
			cam.zoom -= .05;
			inputHandler.scrollDown = false;
		}

	}

	public void update(float delta) {

		if (isGameOver()) {

			Gdx.input.setInputProcessor(null);
			p.currentState = State.DEAD;
			p.isAnimationLooping = false;
			p.body.setLinearVelocity(new Vector2(0f, 0f));

			// just set one timer
			if (!isGameOverSchedualed) {
				System.out.println("Game over!");
				isGameOverSchedualed = true;
			}
		}
		
		if (enemyWaveCounter == 60) {
			enemyWaveCounter = 0;
		} else {
			enemyWaveCounter++;
		}
		
		if((musicStartCount += delta) >= 360 && !isMusicStarted) {
			System.out.println("playing main music");
			mainMusic.play();
			mainMusic.setVolume(0.6f);
			mainMusic.setLooping(true);
			isMusicStarted = true;
		} else {
			musicStartCount++;
		}
		
		if(enemyList.size() <= 0) {
			waveCount++;
		}
		
		// New Wave has begun
		if (waveCount >= currentWave + 1) {
			HUD.displayMessage("Incoming: Wave " + Math.round(waveCount), new Color(1.0f, 1.0f, 1.0f, 1.0f), 2, 3.0f);
			
			currentWave = (int) waveCount;
			waveDefeated = false;
			
			Random rand = new Random();

			try {
				
				if (rand.nextInt(1000 / currentWave) == 0) {
					Gdx.app.log("Event", "A massive army approches your base...");
					long wave = newwave.play();
					newwave.setVolume(wave, 0.3f);
					HUD.displayMessage("A massive army approches your base...");
					
					// -1 will result in spawning the suggested
					// or preset value of enemies
					spawner.spawnHorde(-1);
					
					enemyCount = Math.round((1 + (2 * PlayScreen.currentWave * Player.voltage / Constants.WAVE_VOLTAGE_MULTIPLIER)));
					isWaveSpawning = true;
					
				} else if(!isWaveSpawning) {
					// start wave spawning
					long gensound = generatorfuel.play();
					generatorfuel.setVolume(gensound, 0.3f);
					enemyCount = Math.round((1 + (2 * PlayScreen.currentWave * Player.voltage / Constants.WAVE_VOLTAGE_MULTIPLIER)));
					isWaveSpawning = true;
				}
				
			} catch (Exception e) {
				
				Gdx.app.log("Event", "A massive army approches your base...");
				long wave = newwave.play();
				newwave.setVolume(wave, 0.3f);
				HUD.displayMessage("A massive army approches your base...");
				
				// -1 will result in spawning the suggested
				// or preset value of enemies
				spawner.spawnHorde(-1);
				
				enemyCount = Math.round((1 + (2 * PlayScreen.currentWave * Player.voltage / Constants.WAVE_VOLTAGE_MULTIPLIER)));
				isWaveSpawning = true;
			}
			

			
		}
		
		// if we spawned to capacity for this wave
		// stop spawning
		if(enemyCount <= enemyList.size()) {
			isWaveSpawning = false;
		}
		// If there is room, spawn an enemy
		if(isWaveSpawning && enemyWaveCounter % 30 == 0) {
			spawner.spawnWave();
			
			// Remove any dead enemies
			for(int e = 0; e < enemyList.size(); e++) {
				if(enemyList.get(e).getHp() <= 0.0f) {
					enemyList.remove(e);
					e++;
				}
			}
		}
		
		// Check to see if the player has killed 75% of enemies
		if((enemyCount * (100 - Constants.MINIMUM_ENEMY_PERCENTAGE_TO_CONTINUE) / 100 <= p.enemiesKilled) && (!waveDefeated)) {
			HUD.displayMessage("Wave Defeated!", new Color(1.0f, 1.0f, 1.0f, 1.0f), 2, 3.0f);
			waveDefeated = true;
			p.enemiesKilled = 0;
			
			// If waveCount is not close the next wave,
			// make it close
			if(waveCount - Math.round(waveCount) < 0.85f) {
				waveCount = Math.round(waveCount) + 0.85f;
			}
		}
		
		// Update all of the elapsed time variables
		p.elapsedTime = p.elapsedTime + delta;
		for (int e = 0; e < enemyList.size(); e++) {
			Enemy enemy = enemyList.get(e);
			enemy.elapsedTime = enemy.elapsedTime + delta;
		}

		handleInput();
		
		// Update player
		
		p.update(delta);
		if ((p.elapsedTime > p.getCurrentAnimation().getAnimationDuration()) && (p.isAnimationLooping)) {
			p.setAnimationLooping(false);
			p.elapsedTime = 0.0f;
		}

		// Update all of the enemies
		for (Enemy e : enemyList) {
			e.update(delta);
		}

		world.step(1 / 60f, 6, 2);

		cam.update();
		cam.position.set(new Vector2(p.body.getPosition().x - Constants.DRAW_OFFSET,
				p.body.getPosition().y - Constants.DRAW_OFFSET), 0);
		renderer.setView(cam);
		
	}

	public boolean isGameOver() {

		if (p.getVoltage() <= 0 || p.getHp() <= 0) {
			return true;
		}

		return false;
	}

	@Override
	public void render(float delta) {
		
		gameTime = gameTime + delta;
		update(delta);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render();
				
		game.batch.begin();

		//p.setHp(p.getHp() - 1.0f);

		// TODO: Debug Render
		// Render the collision lines
		//b2dr.render(world, cam.combined);
		
		// Draw the player's current animation
		game.batch.draw(p.getCurrentAnimation().getKeyFrame(p.elapsedTime, p.isAnimationLooping),
				(view.getWorldWidth() / 2), (view.getWorldHeight() / 2), Constants.PLAYER_SIZE / cam.zoom,
				Constants.PLAYER_SIZE / cam.zoom);
		
		// Draw the enemies
		for (Enemy enemy : enemyList) {
			if (enemy.getCurrentAnimation() == null) {
				System.out.println("NULL");
			}

			try {
				game.batch.draw(enemy.getCurrentAnimation().getKeyFrame(enemy.elapsedTime, enemy.isAnimationLooping),
						(enemy.body.getPosition().x - cam.position.x - Constants.DRAW_OFFSET
								+ view.getScreenWidth() / 2),
						(enemy.body.getPosition().y - cam.position.y - Constants.DRAW_OFFSET
								+ view.getScreenHeight() / 2),
						40 / cam.zoom, 40 / cam.zoom);
			} catch (NullPointerException npe) {
				Gdx.app.log("render", "Enemy Animation could not be loaded! State: " + enemy.getCurrentState()
						+ " Elapsed: " + enemy.elapsedTime);
			}
		}

		hud.update(delta, this);

		game.batch.end();

		if (shouldReset) {
			game.setScreen(new MenuScreen(game));
			resetGame();
			
			// Reset static variables
//			waveCount = 1;
//			currentWave = 1;
//			enemyCount = 0;
//			isWaveSpawning = false;

			
		}
		
	}

	@Override
	public void resize(int width, int height) {
		view.update(width, height);
	}

	@Override
	public void dispose() {
		map.dispose();
		world.dispose();
		Assets.dispose();
		// We can use the other one if you want...
	}

	/**
	 * Method that should be called to reset the game. This will include
	 * resetting the player's position, re-loading all items and tiles, and
	 * deleting all enemies.
	 */
	private void resetGame() {
		
		// ---- Reset static variables ----
		// PlayScreen
		System.out.println("Resetting PlayScreen variables...");
		PlayScreen.waveCount = 0.85f;
		PlayScreen.currentWave = 0;
		PlayScreen.enemyCount = 0;
		PlayScreen.isWaveSpawning = false;
		PlayScreen.enemyList.clear();
		
		// Player
		System.out.println("Resetting Player variables...");
		Player.voltage = 10;
		Player.voltageDecay = 3/60f;
		
		// HUD
		HUD.clearMessages();
		
	}

	@Override
	public void show() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	public EnergyCrisisGame getGame() {
		return game;
	}

	public OrthographicCamera getCam() {
		return cam;
	}

	public Viewport getView() {
		return view;
	}

	public OrthogonalTiledMapRenderer getRenderer() {
		return renderer;
	}

	public TmxMapLoader getMapLoader() {
		return mapLoader;
	}

	public TiledMap getMap() {
		return map;
	}

	public Player getPlayer() {
		return p;
	}

}
