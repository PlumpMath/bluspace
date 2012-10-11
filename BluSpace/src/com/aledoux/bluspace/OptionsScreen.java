package com.aledoux.bluspace;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

public class OptionsScreen extends Activity implements OnClickListener, OnSeekBarChangeListener, OnItemSelectedListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_screen);
        
        //set up gui elements and listener
        SeekBar volumeBar = (SeekBar) findViewById(R.id.VolumeBar);
        volumeBar.setProgress((int) (100 * getPreferences(MODE_PRIVATE).getFloat("Volume", 0.5f)));
        volumeBar.setOnSeekBarChangeListener(this);
        
        Spinner laserOptions = (Spinner) findViewById(R.id.LaserControlDropdown);
        laserOptions.setSelection(getPreferences(MODE_PRIVATE).getInt("LaserMode",0));
        laserOptions.setOnItemSelectedListener(this);
        
        Spinner shipOptions = (Spinner) findViewById(R.id.ShipControlDropdown);
        shipOptions.setSelection(getPreferences(MODE_PRIVATE).getInt("ShipMode",0));
        shipOptions.setOnItemSelectedListener(this);
    }

	public void onClick(View v) {
		//TODO
	}

	public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
		if (fromUser && bar.getId() == R.id.VolumeBar){
			SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
			editor.putFloat("Volume", progress / 100f);
			editor.commit();
			GameState.State().UpdateSettings(getPreferences(MODE_PRIVATE));
		}
	}

	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (parent.getId() == R.id.LaserControlDropdown){
			SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
			editor.putInt("LaserMode", pos);
			editor.commit();
			GameState.State().UpdateSettings(getPreferences(MODE_PRIVATE));
		}
		else if (parent.getId() == R.id.ShipControlDropdown){
			SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
			editor.putInt("ShipMode", pos);
			editor.commit();
			GameState.State().UpdateSettings(getPreferences(MODE_PRIVATE));
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

    
}
