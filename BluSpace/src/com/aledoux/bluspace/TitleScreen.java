package com.aledoux.bluspace;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
		}
	}

    
}
