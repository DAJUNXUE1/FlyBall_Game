package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MainScreen extends ScreenAdapter {


    BalloonGame game;

    private SpriteBatch batch;

    private TextureAtlas atlas;
    private TextureRegion background;
    private OrthographicCamera camera;

    private TextureRegion darkCloud;
    private TextureRegion grassLand;

    private TextureRegion hill;
    private TextureRegion lightting;

    private float scrollValue;

    private  TextureRegion[] balloonArray;
    private  Animation balloon;
    private float stateTime;

    private TextureRegion touchRegion;
    private TextureRegion tapRegion;

    private float touchTime = 0;
    final  float maxTime = 1.0f;

    private Vector3 touchPosition = new Vector3();

    private BitmapFont font;

    // 飞行速度
    private Vector2 speed = new Vector2();
    // 当前位置
    private Vector2 currentPosition = new Vector2();
    // 上一帧位置
    private  Vector2 lastPosition = new Vector2();
    // 重力
    private  Vector2 gravity = new Vector2();
    // 空气阻力
    private  final Vector2 friction = new Vector2(1,1);

    private  Vector2 scrollSpeed = new Vector2(2,0);

    private Vector2 tempVec = new Vector2();

    // 障碍物数组
    private Array<Vector2> barriers = new Array<Vector2>();

    // 上一帧上次的障碍物的向量值
    private Vector2 barrierPosition = new Vector2();

    private float distance = 0;

    private final  float HILL = 2;
    private final  float LIGHTING = 3;

    private GameState gameState = GameState.INIT;
    private TextureRegion gameOver;

    private Vector2 initVelocity = new Vector2();



    private Array<PickUp> pickUpsInScene = new Array<PickUp>();
    private PickUp tempPickUp;
    private Vector2 pickUpTiming = new Vector2();


    public int heartCount = 0;
    public int oilCount = 0;


    private float score = 0;

    private Texture bottle;
    private float bottlePercent;


    public MainScreen(BalloonGame balloonGame) {
        game = balloonGame;
    }

    @Override
    public void show() {
        atlas = game.atlas;
        camera = game.camera;
        batch = game.batch;

        font = game.font;
        // 背景
        background = atlas.findRegion("game_bg");
        darkCloud = atlas.findRegion("darkclouds");
        grassLand = atlas.findRegion("grass");

        // start
        touchRegion = atlas.findRegion("touch");
        // action
        tapRegion = atlas.findRegion("tap");

        gameOver = atlas.findRegion("gameover");

        hill = atlas.findRegion("hill");
        lightting = atlas.findRegion("lightning");

        balloonArray = new TextureRegion[3];
        balloonArray[0] = atlas.findRegion("balloon1");
        balloonArray[1] = atlas.findRegion("balloon2");
        balloonArray[2] = atlas.findRegion("balloon3");

        balloon = new Animation(0.05f,balloonArray);

        bottle = new Texture(Gdx.files.internal("main/bottle.png"));


        this.InitValue();
    }

    // 初始化游戏数值
    private  void InitValue(){

        scrollValue = 0;
        stateTime = 0;

        speed.set(350.0f,0);
        gravity.set(0,-9.0f);

        // 初始位置
        lastPosition.set(206,240);

        // 当前位置
        currentPosition.set(lastPosition.x,lastPosition.y);

        barriers.clear();
        // 重置障碍物的位置，修复bug代码
        barrierPosition.setZero();

        initVelocity.set(4,0);


        score = 0;
        oilCount = 100;
        bottlePercent = 114;
        heartCount = 0;

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        CreatPickUps(delta);

        this.gameLogic();

        this.gameRender();
    }
    //处理游戏逻辑
    public void gameLogic(){

        // 触屏监听
        if(Gdx.input.justTouched()){

            if(gameState == GameState.INIT){
                gameState = GameState.ACITION;
                return;
            }

            if(gameState == GameState.GAME_OVER){
                gameState = GameState.INIT;
                resetScene();
                return;
            }

            if(oilCount <=0) return;

            touchPosition.set(Gdx.input.getX(),Gdx.input.getY(),0);

            camera.unproject(touchPosition);

            tempVec.set(currentPosition.x,currentPosition.y);

            tempVec.sub(touchPosition.x,touchPosition.y).nor();

            speed.mulAdd(tempVec,450);

            touchTime = maxTime;
        }

        if(gameState == GameState.INIT || gameState == GameState.GAME_OVER){
            return;
        }

        if(currentPosition.y < 35 || currentPosition.y > 409){
            gameState = GameState.GAME_OVER;
        }


        // 积分处理
        score +=Gdx.graphics.getDeltaTime();
        oilCount -= 6 * Gdx.graphics.getDeltaTime();
        bottlePercent = bottle.getWidth() * oilCount / 100;


        speed.add(initVelocity);

        touchTime -=Gdx.graphics.getDeltaTime();

        stateTime +=Gdx.graphics.getDeltaTime();

        speed.scl(friction);

        speed.add(gravity);

        speed.add(scrollSpeed);

        currentPosition.mulAdd(speed,Gdx.graphics.getDeltaTime());


        distance = currentPosition.x  - lastPosition.x;


        Rectangle balloonRect = new Rectangle();

        balloonRect.set(currentPosition.x + 22, currentPosition.y + 18, 44, 36);

        Rectangle objectRect = new Rectangle();
        

        // 移动与销毁
        for (Vector2 vec : barriers) {
            vec.x -=distance;
            if(vec.x  + hill.getRegionWidth() < -25){
                barriers.removeValue(vec,false);
            }

            if(vec.y==HILL)
            {

                objectRect.set(vec.x + 10, 0, hill.getRegionWidth()-20,
                        hill.getRegionHeight()-10);
            }
            else
            {
                objectRect.set(vec.x + 10,
                        480 - lightting.getRegionHeight()+10,
                        lightting.getRegionWidth()-20, lightting.getRegionHeight());
            }


            if(balloonRect.overlaps(objectRect))
            {
                if(gameState != GameState.GAME_OVER)
                {
                     gameState = GameState.GAME_OVER;
                }
            }

        }

        for (PickUp p : pickUpsInScene) {

            p.pickUpPosition.x -=distance;

            if(p.pickUpPosition.x  + p.pickUpTexture.getRegionWidth() < -25){
                pickUpsInScene.removeValue(p,false);
            }


            objectRect.set(p.pickUpPosition.x,p.pickUpPosition.y,
                    p.pickUpTexture.getRegionWidth(),p.pickUpTexture.getRegionHeight());

            if(balloonRect.overlaps(objectRect))
            {
                PickIt(p);
            }

        }


        // 创建
        if(barrierPosition.x < 350){
            this.createBarrier();
        }


        scrollValue -=currentPosition.x - lastPosition.x;
        currentPosition.x = lastPosition.x;

        if(scrollValue < -grassLand.getRegionWidth()) scrollValue = 0;
        if(scrollValue > 0) scrollValue =  -grassLand.getRegionWidth();

    }



    //处理游戏渲染
    public  void gameRender(){
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        this.drawBg();

        this.drawBarriers();

        this.drawClouds();

        this.drawBalloon();

        this.drawPickUps();


        if(gameState == GameState.INIT){
            batch.draw(touchRegion,currentPosition.x,currentPosition.y - 80);
        }

        if(gameState == GameState.GAME_OVER){
            batch.draw(gameOver,400 - gameOver.getRegionWidth() / 2,240 - gameOver.getRegionHeight() / 2);
        }

        if(touchTime > 0 && gameState == GameState.ACITION){
            batch.draw(tapRegion,touchPosition.x - tapRegion.getRegionWidth() / 2,touchPosition.y - tapRegion.getRegionHeight() / 2);
        }

        if(gameState == GameState.ACITION)
            game.font.draw(batch,String.format("%d",(int)(heartCount + score)),700,400);

        batch.setColor(Color.BLACK);
        batch.draw(bottle,30,300);

        batch.setColor(Color.WHITE);
        batch.draw(bottle,30,300,0,0,(int)bottlePercent,bottle.getHeight());
        batch.end();
    }


    private void createBarrier(){

        // 当前即将生成的障碍物矢量
        Vector2 barrierCurrentPosition = new Vector2();

        if(barriers.size == 0){

                barrierCurrentPosition.x = (float)(800 + Math.random() * 600);
        }else {
            barrierCurrentPosition.x = barrierPosition.x +  (float)(550 + Math.random() * 550);
        }
// 障碍物类型设置
        if(Math.random() > 0.5f){

            barrierCurrentPosition.y = HILL;
        }else {
            barrierCurrentPosition.y = LIGHTING;
        }

        barrierPosition = barrierCurrentPosition;

        barriers.add(barrierCurrentPosition);
    }

    private void drawPickUps(){
        for (PickUp p : pickUpsInScene) {
            batch.draw(p.pickUpTexture,p.pickUpPosition.x,p.pickUpPosition.y);
        }
    }


    private void drawBg(){
        batch.disableBlending();
        batch.draw(background,0,0);
        batch.enableBlending();
    }

    private void drawClouds(){
//第一张草地
        batch.draw(grassLand,scrollValue,0);
//第二张草地
        batch.draw(grassLand,scrollValue + grassLand.getRegionWidth(),0);
//第一张乌云
        batch.draw(darkCloud,scrollValue,409);
//第二张乌云
        batch.draw(darkCloud,scrollValue + darkCloud.getRegionWidth(),409);
    }

    private void drawBarriers(){

        if(gameState == GameState.INIT) return;

        for (Vector2 barrier : barriers) {

            if(barrier.y == HILL){

                batch.draw(hill,barrier.x,0);

            }else{

                batch.draw(lightting,barrier.x,480 - lightting.getRegionHeight());
            }
        }
    }
    // 绘制主角动画
    private void drawBalloon(){
        TextureRegion ballonAni = (TextureRegion) balloon.getKeyFrame(stateTime,true);
        batch.draw(ballonAni,currentPosition.x,currentPosition.y);

        font.draw(batch,"hello world!",350,200);
    }


    // 重置游戏场景资源等
    private void resetScene(){
        InitValue();
    }

    @Override
    public void dispose() {

    }

    private boolean addPickUp(int type){

        Vector2 randomPosition = new Vector2();
        randomPosition.x = 820;
        randomPosition.y = (float) (80 + Math.random() * 300);

        Rectangle objRect = new Rectangle();

        for (Vector2 vec : barriers) {

            if(vec.y==HILL)
            {
                objRect.set(vec.x + 10, 0, hill.getRegionWidth()-20,
                        hill.getRegionHeight()-10);
            }
            else
            {
                objRect.set(vec.x + 10,
                        480 - lightting.getRegionHeight()+10,
                        lightting.getRegionWidth()-20, lightting.getRegionHeight());
            }

            if(objRect.contains(randomPosition))
            {
                return  false;
            }
        }

        tempPickUp = new PickUp(type,game.assetManager);
        tempPickUp.pickUpPosition = randomPosition;
        pickUpsInScene.add(tempPickUp);

        return  true;
    }

    public  void PickIt(PickUp p){

        p.pickUpSound.play();

        switch (p.pickUpType){
            case PickUp.Heart:

                heartCount +=p.pickUpValue;

                break;

            case PickUp.Oil:

                oilCount +=p.pickUpValue;
                if(oilCount >= 100)oilCount = 114;
                break;
        }

        pickUpsInScene.removeValue(p,false);
    }

    private void CreatPickUps(float delta){

        pickUpTiming.sub(delta,delta);

        if(pickUpTiming.x <=0){

            if(addPickUp(PickUp.Heart)){
                pickUpTiming.x = 1 + (float) Math.random() * 2;
            }else {
                pickUpTiming.x =  (float) (0.5f +Math.random() * 0.5f);
            }
        }

        if(pickUpTiming.y <=0){

            if(addPickUp(PickUp.Oil)){
                pickUpTiming.y = 3 + (float) Math.random() * 2;
            }else {
                pickUpTiming.y =  (float) (0.5f +Math.random() * 0.5f);
            }
        }
    }

}

enum GameState{
    INIT,GAME_OVER,ACITION
}
