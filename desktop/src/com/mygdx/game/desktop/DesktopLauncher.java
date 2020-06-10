package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Util.FlyBallGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
	    // 将对象配置
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    //
		LwjglApplication lwgj =  new LwjglApplication(new FlyBallGame(),config);
	}
}
