package com.connectike.energycrisis.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.connectike.energycrisis.EnergyCrisisGame;
import com.connectike.energycrisis.PlayScreenInputHandler;
import com.connectike.energycrisis.entities.Player;
import com.connectike.energycrisis.tools.WorldContactListener;

public class PlayScreen implements Screen {
	
	private EnergyCrisisGame game;
	
	private OrthographicCamera cam;
	private Viewport view;
	
	OrthogonalTiledMapRenderer renderer;
	TmxMapLoader mapLoader;
	
	private PlayScreenInputHandler inputHandler;
	
	// map and worlds
	private World world;
	private TiledMap map;
	private Box2DDebugRenderer b2dr;
	
	private Player p;
	FPSLogger logger;
	
	private float elapsedTime;
	
	private float mapWidth, mapHeight = 0.0f;
	
	public PlayScreen(EnergyCrisisGame game) {
		
		this.game = game;
		
		logger = new FPSLogger();
		
		// set a camera and bind a viewport to it
		cam = new OrthographicCamera();
		view = new ScreenViewport(cam);
		
		// load our map with the TmxMapLoader
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("maps/lvl1.tmx");
		
		renderer = new OrthogonalTiledMapRenderer(map);
		
		p = new Player(0, 0, 0, 0);
		
		inputHandler = new PlayScreenInputHandler(p);
		Gdx.input.setInputProcessor(inputHandler);
		
		world = new World(new Vector2(0, 0), true);
		b2dr = new Box2DDebugRenderer();
		
		world.setContactListener(new WorldContactListener());
		
		// get the width and height of the map (in tile count)
		mapWidth = Float.valueOf((Integer) map.getProperties().get("width"));
		mapHeight = Float.valueOf((Integer) map.getProperties().get("height"));
		// multiply by tile size (ex. 32x32)
		cam.translate(mapWidth / 2 * 32, mapHeight / 2 * 32);
		
	}
	
	public void handleInput() {
		
		if(inputHandler.isW) {
			p.currentState = Player.State.UP;
			cam.translate(0, 5);
		}
		if(inputHandler.isA) {
			p.currentState = Player.State.LEFT;
			cam.translate(-5, 0);
		}
		if(inputHandler.isS) {
			p.currentState = Player.State.DOWN;
			cam.translate(0, -5);
		}
		if(inputHandler.isD) {
			p.currentState = Player.State.RIGHT;
			cam.translate(5, 0);
		}
		
		if(inputHandler.scrollUp) {
			cam.zoom += .05;
			inputHandler.scrollUp = false;
		}
		if(inputHandler.scrollDown) {
			cam.zoom -= .05;
			inputHandler.scrollDown = false;
		}
		
	}
	
	public void update() {
		
		handleInput();
		
		cam.update();
		renderer.setView(cam);
	}
	
	@Override
	public void render(float delta) {
		
		logger.log();
		
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.render();
		
		game.batch.begin();
		
		// Draw the player's current animation
		game.batch.draw(p.getCurrentAnimation().getKeyFrame(elapsedTime, p.isAnimationLooping), cam.viewportWidth / 2,
		        cam.viewportHeight / 2, 32 / cam.zoom, 32 / cam.zoom);
		
		game.batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		view.update(width, height);
	}
	
	@Override
	public void dispose() {
		map.dispose();
		p.dispose();
		world.dispose();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
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
	
}
