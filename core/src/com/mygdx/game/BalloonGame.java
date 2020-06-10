package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class BalloonGame extends Game {

	public AssetManager assetManager;
	private FPSLogger logger;

	public SpriteBatch batch;
	public OrthographicCamera camera;
	public StretchViewport viewport;
	public TextureAtlas atlas;
	public static final int width = 800;
	public static final int height = 480;

	public BitmapFont font;

	public  BalloonGame(){
		assetManager = new AssetManager();
		logger = new FPSLogger();

		camera = new OrthographicCamera();
		camera.position.set(width/2,height/2,0);
		viewport = new StretchViewport(width,height,camera);

	}


	@Override
	public void create () {

		// 资源加载
		this.LoadSource();
		batch = new SpriteBatch();
		atlas = assetManager.get("main/test.atlas",TextureAtlas.class);
		font = assetManager.get("font/test.fnt",BitmapFont.class);
		setScreen(new StartScreen(this));
//	    setScreen(new MainScreen(this));
//		setScreen(new LoadScreen(this));
	}

	@Override
	public void render () {
        super.render();
//        logger.log();
//        System.out.print(assetManager.getProgress());
	}


	public void LoadSource(){

		// 加载字体
		assetManager.load("font/test.fnt", BitmapFont.class);
		// 图集资源
		assetManager.load("main/test.atlas", TextureAtlas.class);
		// 加载音频文件
		assetManager.load("audio/boom.mp3", Sound.class);
		assetManager.load("audio/bottle.wav", Sound.class);
		assetManager.load("audio/click.mp3", Sound.class);
		assetManager.load("audio/heart.mp3", Sound.class);
		assetManager.load("audio/oil.wav", Sound.class);
		assetManager.load("audio/pop.ogg", Sound.class);
		//加载音乐文件
		assetManager.load("audio/main_bg.mp3", Music.class);
		assetManager.load("audio/start_bg.mp3", Music.class);

		// 加载load场景的资源
		assetManager.load("load/loadbg.jpg", Texture.class);
		// 帧动画
		for (int i = 1;i < 25;i++){
			assetManager.load("load/"+ i +".jpg", Texture.class);
		}


		// 加载完成资源后，阻塞异步线程
		assetManager.finishLoading();

	}


	@Override
	public void resize(int width, int height) {
		viewport.update(width,height);
	}

	@Override
	public void dispose () {

		batch.dispose();
		assetManager.dispose();
		atlas.dispose();
	}
}
