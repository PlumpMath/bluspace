package com.aledoux.bluspace;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class TitleScreen extends Activity implements OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //turn off the title bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //request portrait orientation
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.title_screen);
        
        //set up button listeners
        findViewById(R.id.MultiplayerButton).setOnClickListener(this);
        findViewById(R.id.SingleplayerButton).setOnClickListener(this);
        findViewById(R.id.OptionsButton).setOnClickListener(this);
        findViewById(R.id.CreditsButton).setOnClickListener(this);
        
        //set up user preferences
        initSettings();
        
        MediaPlayer mp;
        mp = MediaPlayer.create(getApplicationContext(), R.raw.theme);
        mp.setLooping(true);
        try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mp.start();
    }
    
    /**
     * If the preference settings have never been set before, initialize them to their default values
     * also, give the GameState a reference to the settings
     */
    public void initSettings(){
    	SharedPreferences settings = getPreferences(MODE_PRIVATE);
    	SharedPreferences.Editor editor = settings.edit();
    	
    	if (settings.getFloat("Volume", -1f) < 0){
    		editor.putFloat("Volume", 0.5f);
    	}
    	
    	if (settings.getInt("LaserMode", -1) < 0){
    		editor.putInt("LaserMode",0);
    	}
    	
    	if (settings.getInt("ShipMode", -1) < 0){
    		editor.putInt("ShipMode",0);
    	}
    	
    	editor.commit();
    	
    	GameState.State().UpdateSettings(settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title_screen, menu);
        return true;
    }

	public void onClick(View v) {
		Intent i;
		switch (v.getId()){
			case R.id.MultiplayerButton:      
                i = new Intent(this, BluetoothChat.class);
                startActivity(i);
				break;
			case R.id.SingleplayerButton:
				i = new Intent(this, GameScreen.class);
				startActivity(i);
				//restart the game
				GameState.State().Restart(new MainGameLogic());
				break;
			case R.id.OptionsButton:
				i = new Intent(this, OptionsScreen.class);
				startActivity(i);
				break;
            case R.id.CreditsButton:
				i = new Intent(this, CreditsScreen.class);
				startActivity(i);
                break;  
		}
	}

    
}
