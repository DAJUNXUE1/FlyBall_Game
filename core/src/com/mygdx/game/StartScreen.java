package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class StartScreen extends ScreenAdapter {


    BalloonGame game;

//    private SpriteBatch batch = new SpriteBatch();
//    private BitmapFont font = new BitmapFont();

    private Stage stage;
    private Image bg;
    private Image start_button;
    private TextureRegion texture;
    private Viewport stretch_viewport;
    private Music music;
    private Sound sound;
    private TextureAtlas atlas;


    public StartScreen(BalloonGame balloonGame) {
        game = balloonGame;

        atlas = game.assetManager.get("main/test.atlas",TextureAtlas.class);
        texture = atlas.findRegion("start_bg");
        start_button = new Image(atlas.findRegion("start_btn"));

        music = game.assetManager.get("audio/start_bg.mp3",Music.class);
        music.setLooping(true);
        music.play();

        sound = game.assetManager.get("audio/click.mp3",Sound.class);

        bg = new Image(texture);

        start_button.setPosition(470,220);

        stretch_viewport = new StretchViewport(800,480);

        stage = new Stage(stretch_viewport);
        stage.addActor(bg);
        stage.addActor(start_button);

        start_button.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {


                sound.play();
                game.setScreen(new LoadScreen(game));

                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        batch.begin();
//        font.draw(batch, "START SCREEN", 200, 200);
//        batch.end();
        stage.act();
        stage.draw();


    }

    @Override
    public void hide() {
        music.pause();
    }

    @Override
    public void dispose() {

    }
}
