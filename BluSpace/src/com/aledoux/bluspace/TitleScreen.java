package com.aledoux.bluspace;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class TitleScreen extends Activity implements OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_screen);
        
        //set up button listeners
        findViewById(R.id.StartGame).setOnClickListener(this);
        findViewById(R.id.RestartGame).setOnClickListener(this);
        findViewById(R.id.Options).setOnClickListener(this);
        
        //set up user preferences
        initSettings();
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
			case R.id.StartGame:
				i = new Intent(this, GameScreen.class);
				startActivity(i);
				//start the game
				GameState.State().Start(new MainGameLogic());
				break;
			case R.id.RestartGame:
				i = new Intent(this, GameScreen.class);
				startActivity(i);
				//restart the game
				GameState.State().Restart(new MainGameLogic());
				break;
			case R.id.Options:
				i = new Intent(this, OptionsScreen.class);
				startActivity(i);
				break;
		}
	}

    
}
