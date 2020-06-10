package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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

public class PickUp {

    public static  final int Heart = 1;
    public static  final int Oil =2;

    public TextureRegion pickUpTexture;
    public int pickUpType = 0;

    public Vector2 pickUpPosition = new Vector2();

    public int pickUpValue;
    public Sound pickUpSound;


    public PickUp(int type,AssetManager assetManager){

        TextureAtlas atlas = assetManager.get("main/test.atlas",TextureAtlas.class);

        pickUpType = type;

        switch (type){
            case  Heart:

                pickUpTexture = atlas.findRegion("heart");
                pickUpValue = 5;
                pickUpSound =assetManager.get("audio/heart.mp3", Sound.class);

                break;

            case Oil:

                pickUpTexture = atlas.findRegion("bottle");
                pickUpValue = 100;
                pickUpSound = assetManager.get("audio/oil.wav", Sound.class);

                break;
        }
    }


}
