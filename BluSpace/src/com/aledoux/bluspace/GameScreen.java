package com.aledoux.bluspace;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

public class GameScreen extends Activity {
	GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //turn off the title bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //request portrait orientation
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        //create a game view
        gameView = new GameView(this);
        setContentView(gameView);
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	gameView.onResume();
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	gameView.onPause();
    }
    
}
