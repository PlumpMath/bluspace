package com.aledoux.bluspace;

/*
 * MEGA SUPER DEPRECATED DO NOT USE EVER
 */

import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 *
 * @author Shane
 */
public class BluetoothActivity extends Activity implements OnClickListener{

    public final static int REQUEST_ENABLE_BT = 13666;
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) 
    {
		super.onCreate(icicle);
		
		//turn off the title bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //request portrait orientation
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
		//Set the UI File 
		setContentView(R.layout.bluetooth_activity);
		
		//Set Up action Listener for the buttons
	    findViewById(R.id.HostAGame).setOnClickListener(this);
	    findViewById(R.id.JoinGame).setOnClickListener(this);
	    
	    Bluetooth.adapter = BluetoothAdapter.getDefaultAdapter(); 
	  
	    if(Bluetooth.adapter == null)
	    {
	      //Whoops.. We have a problem here... 
	    	Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show();
	    }
	   
	    if (!Bluetooth.adapter.isEnabled())
	    {
	        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	    }
       
        registerReceiver(Bluetooth.broadcastListener, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (requestCode == REQUEST_ENABLE_BT){
    		if (resultCode == RESULT_OK){
    			Log.i("bluetooth","success");
    		}
    		else{ Log.i("bluetooth", "failure");}
    	}
    }
    
    public void onClick(View view)
    {
        //Handle the button clicks:
        int buttonID = view.getId();
        Log.i("helllooooo","wtf");
        switch(buttonID)
        {
            case R.id.HostAGame: 

                GameState.State().isHost = false;
                Log.i("host1", "host " + GameState.State().isHost);
                //Make Discoverable
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
                Bluetooth.makeServer();
                    
                break;
                
            case R.id.JoinGame:
            	Log.i("what the fuck","wtf");
            	GameState.State().isHost = false;
                Bluetooth.makeClient();
                
                Log.i("host1", "host " + GameState.State().isHost);
                break;
        }
        Log.i("host1", "host " + GameState.State().isHost);
       
    }
    
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(Bluetooth.broadcastListener);
        Bluetooth.uninit();
    }
}
