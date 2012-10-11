package com.aledoux.bluspace;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * A view that can be drawn onto
 * and has its own main loop
 * made using a thread.
 * 
 * It also capture touch events
 * that can be used by the game logic.
 * 
 * @author Adam Le Doux
 *
 */
public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback, OnTouchListener, SensorEventListener{
	
	Context context;
	Thread thread;
	SurfaceHolder surfaceHolder;
	volatile boolean running;
	volatile boolean surfaceReady; //can we draw on the surface?
	
	//accelerometer variables
	SensorManager sm;
	Sensor accelerometer;

	public GameView(Context context) {
		super(context);
		
		this.context = context;
		GameState.State().setContext(context);
		
		this.setOnTouchListener(this);
		
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this); //listens for events from the surface
		
		running = false;
		surfaceReady = false;
		
		//get accelerometer
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	public void run() {
		while (running){
			if(surfaceReady){
				//update game state
				GameState.State().update();
				
				//get canvas
				Canvas canvas = surfaceHolder.lockCanvas();
				
				//render game
				GameState.State().render(canvas);
				
				//update canvas
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	//resume the thread
	//mark it as "running"
	//and create a new thread with the GameView as its "runnable" object
	public void onResume(){
		running = true;
		
		//restart the thread
		thread = new Thread(this);
		thread.start();
		
		//reset the time step when we unpause
		GameState.State().resetTimeStep();
		
		//turn the sensors back on when we unpause
		sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	//pause the thread
	public void onPause(){
		running = false;
		sm.unregisterListener(this); //turn of the sensors when we pause (conserver battery)
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		//for if the surface changes width and height
		GameState.State().setScreenSize(width, height);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		surfaceReady = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		surfaceReady = false;
	}

	public boolean onTouch(View v, MotionEvent event) {
		
		
		GameState.State().touchInput(event);
		
		//send the touch input to the game's logic
		
		
		return true;
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		
		GameState.State().acceleromaterInput(x, y, z);
	}
	
//	public boolean onTouch(View v, MotionEvent event) {
//		int touchX = (int) event.getX();
//		int touchY = (int) event.getY();
//		
//		GameState.State().touchInput(touchX, touchY);
//		
//		//send the touch input to the game's logic
//		
//		
//		return true;
//	}
	
}