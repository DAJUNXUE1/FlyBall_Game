package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LoadScreen extends ScreenAdapter {


    BalloonGame game;

    private SpriteBatch batch;
    private Texture bg;

    private Animation animation;

    private Texture[] regions;
    private TextureRegion[] aniRegions;


    private float timer = 0;


    public LoadScreen(BalloonGame balloonGame) {

        game = balloonGame;

        batch = game.batch;
        bg = game.assetManager.get("load/loadbg.jpg",Texture.class);

        regions = new Texture[24];
        aniRegions = new TextureRegion[regions.length];

        for (int i = 0; i < regions.length;i++){

            regions[i] = game.assetManager.get("load/"+(i + 1)+".jpg",Texture.class);
            aniRegions[i] = new TextureRegion(regions[i]);
        }

        animation = new Animation(0.05f,aniRegions);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        float deltaTime = Gdx.graphics.getDeltaTime();

        timer +=deltaTime;

        if(timer > 5){
            game.setScreen(new MainScreen(game));
        }


        batch.begin();

        batch.draw(bg,0,0);

        TextureRegion texture = (TextureRegion) animation.getKeyFrame(timer,true);
        batch.draw(texture,200,90);

        batch.end();


    }

    @Override
    public void dispose() {

    }
}