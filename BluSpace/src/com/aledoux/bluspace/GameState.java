package com.aledoux.bluspace;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.PriorityQueue;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.util.Log;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;

import android.graphics.Paint;
import android.graphics.Color;


import android.view.MotionEvent;

/**
 * Stores all the logic for the game
 * draws it onto the canvas,
 * and receives input from the screen.
 * 
 * GameState follows a singleton pattern,
 * so that as long as the app is open
 * only a single game state is maintained.
 * 
 * @author Adam Le Doux
 *
 */
public class GameState {
	/**
	 * STATE AND LOGIC VARIABLES
	 */
	private static GameState GAME_STATE; //current state of the game (singleton pattern)
	private LogicObject mainLogic; //the main logic object for the current game state
	private long deltaTime; //time between current frame and previous frame (in milliseconds)
	private long prevFrameTime; //time the previous frame occurred
	
	/**
	 * SCREEN VARIABLES
	 */
	//size of the screen (represented as a Vector)
	Vector ScreenSize;
	//camera
	private Camera camera;
	private Point cameraPosition, prevCameraPosition;
	//context
	public Context context;
	
	/**
	 * TOUCH VARIABLES
	 */
	//have we received a touch input this frame? last frame
	private boolean touched, prevTouched;
	//the last location that was touched
	public Point lastTouch, firstTouch;
	//the last times the screen was touched
	public long touchTime, prevTouchTime;
	//the time delay needed for something to count as a distinct tap OR a long press (in milliseconds)
	public long tapInterval, pressInterval;
	//when was the last time a touch was lifted?
	public long lastLiftTime;
	public Paint paint;
	
	/**
	 * AUDIO VARIABLES
	 */
	private SoundPool soundPool;
	private float volume;
	private Hashtable<String,Integer> soundTable; //use names to get soundIDs
	private Hashtable<String,Integer> streamTable; //use names to get streamIDs
	
	/**
	 * MOTION SENSOR VARIABLES
	 */
	private float accX, accY, accZ; //last accelerometer values
	private float accDX, accDY, accDZ; //change in accelerometer values
	private float accNoise; //the amount of "noise" we cut in our accelerometer changes
	
	/**
	 * SETTINGS / OPTIONS VARIABLES
	 */
	private SharedPreferences settings;
	public static enum ShipControl {Target, Joystick, Swipe};
	public static enum LaserControl {Shake, TouchShip, TouchScreen};
	private ShipControl shipMode;
	private LaserControl laserMode;
	
	/**
	 * BLUETOOTH VARIABLES
	 */
	//private String lastBluetoothMessage;
	private int[] lastBluetoothMessage;
	private boolean isMsgRead; //has the latest bluetooth message been read?
	public boolean isHost = true; //hosting the game?
	
	public GameState(){
		gameStateStartValues();
	}
	
	private void gameStateStartValues(){
		touched = false;
		prevTouched = false;
		tapInterval = 150; //sensitivity to player taps (in milliseconds)
		pressInterval = 500;
		camera = new Camera();
		prevFrameTime = (new Date()).getTime();
		deltaTime = 0;
		accNoise = 8.0f;

		paint = new Paint();

		cameraPosition = new Point();
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		soundTable = new Hashtable<String,Integer>();
		streamTable = new Hashtable<String,Integer>();

		paint.setColor(Color.WHITE);
		
		//null values
		lastTouch = null;
		ScreenSize = null;
		
		/**
		 * SET OPTIONS
		 */
		if (settings != null){
			UpdateSettingValues();
		}
		else{
			volume = 0.1f;
			shipMode = ShipControl.Swipe;
			laserMode = LaserControl.TouchShip;
		}
	}
	
	//accessor for the game state (singleton pattern)
	public static GameState State(){
		if (GAME_STATE == null){
			GAME_STATE = new GameState();
		}
		return GAME_STATE;
	}
	
	/**
	 * kills all current logic and render objects
	 * and starts again with a new main logic process
	 * @param newLogic
	 */
	public void Restart(LogicObject newLogic){
		gameStateStartValues(); //reset values of the game state
		
		//destroy everything
		for (Renderable r : RenderObject.getRenderables()){
			RenderObject.destroy(r);
		}
		for (Updateable u : LogicObject.getLogic()){
			LogicObject.destroy(u);
		}
		GameState.cull();
		
		//add a new logic object to the update queue
		LogicObject.getLogic().add(newLogic);
		
		this.mainLogic = newLogic;
	}
	
	/**
	 * A "softer" version of restart. It only
	 * uses the new logic if nothing else already exists to overwrite.
	 * 
	 * @param mainLogic
	 */
	public void Start(LogicObject newLogic){
		if (this.mainLogic == null){
			Restart(newLogic);
		}
		else{
			//must destroy new logic (if not needed) to prevent duplicates
			LogicObject.getLogic().remove(newLogic);
		}
	}
	
	public void update(){
		//update previous camera position
		prevCameraPosition = new Point(cameraPosition);
		
		//update all updateable game logic objects
		PriorityQueue<Updateable> queue = LogicObject.getUpdateQueue();
		while (!queue.isEmpty()){
			queue.poll().update();
		}
		
		//destroy all game objects, render objects, and logic objects that need to be destroyed
		GameState.cull();
		
		//update touch status variables
		updateTouchStatus();
		
		//update camera
		Vector cameraTrans = new Vector(prevCameraPosition, cameraPosition);
		cameraTrans.x = -cameraTrans.x; //android's coordinate system works is backwards
		camera.translate(cameraTrans.x, cameraTrans.y, 0);
		
		//update time step
		deltaTime = (new Date()).getTime() - prevFrameTime;
		prevFrameTime = (new Date()).getTime();
	}
	
	public void render(Canvas canvas){
		//clear canvas
		canvas.drawColor(0xff000000);
		
		//apply camera
		camera.applyToCanvas(canvas);
		
		//draw all renderable objects
		PriorityQueue<Renderable> queue = RenderObject.getRenderQueue();
		while (!queue.isEmpty()){
			queue.poll().render(canvas);
		}
		
		/**
		 * This is probably not the best place to draw this but I'm leaving it for now --Adam
		 */
		/**
		 * Draws the line we move ourselves with. I DID THIS
		 * @author Sean Wheeler
		 */
		if(firstTouch != null && lastTouch != null && touched == true)
		{
			canvas.drawLine(firstTouch.x+GetCameraPosition().x, firstTouch.y+GetCameraPosition().y, lastTouch.x+GetCameraPosition().x, lastTouch.y+GetCameraPosition().y, paint);
		}
	}
	
	/**
	 * remove all logic objects, renderable objects, and (by extension) game objects that need to be destroyed
	 */
	public static void cull(){
		RenderObject.cull();
		LogicObject.cull();
	}
	
	public void updateTouchStatus(){
		if (!touched){
			lastLiftTime = new Date().getTime();
		}
		if (screenTapped()){
			prevTouchTime = touchTime;
		}
		prevTouched = touched; //save the state of the last frame's touched variable
		
		
		/**
		 * I commented this out so we can try the toggle system
		 * @author Sean Wheeler
		 */
		//touched = false; //set the current state's touched variable to false (innocent until proven guilty)
		
		
	}

	
	/**
	 * Gets touch input from outside
	 * @param x
	 * @param y
	 */
//	public void touchInput(int x, int y){
//		//update record of last touch position
//		lastTouch = new Point(x,y);
//		//note that we've been touched
//		touched = true;
//		//note time we've been touched
//		touchTime = new Date().getTime();
//		
//		if (prevTouched == false)
//		{
//			firstTouch = new Point(x,y);
//		}
//		
//	}
	
	public void touchInput(MotionEvent event){
		
		int x = (int) event.getX();
		int y = (int) event.getY();
		//update record of last touch position
		lastTouch = new Point(x,y);
		//note that we've been touched
		touched = true;
		
		/**
		 * I put this here to toggle touched as a boolean
		 * @author Sean Wheeler
		 */
		if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL)
		{
			touched = false;
		}
		
		//note time we've been touched
		touchTime = new Date().getTime();
		
		/**
		 * This creates a point for our touch drag movement
		 * @author Sean Wheeler
		 */
		if (prevTouched == false && event.getActionMasked() == MotionEvent.ACTION_DOWN)
		{
			firstTouch = new Point(x,y);
		}
		
	}
	
	/**
	 * Recieve accelerometer input from the phone (via the GameView)
	 * @param x
	 * @param y
	 * @param z
	 */
	public void acceleromaterInput(float x, float y, float z){
		accDX = Math.abs(accX - x);
		accDY = Math.abs(accY - y);
		accDZ = Math.abs(accZ - z);
		
		accX = x;
		accY = y;
		accZ = z;
	}
	
	/**
	 * is someone shaking the phone?
	 * @return
	 */
	public boolean isShaking(){
		return (accDX > accNoise) || (accDY > accNoise) || (accDZ > accNoise);
	}
	
	
	/**
	 * True if the user has waited long enough between touches
	 * @return
	 */
	public boolean screenTapped(){
		return (touched && !prevTouched) && (Math.abs((new Date().getTime()) - prevTouchTime) > tapInterval);
	}
	
	/**
	 * True if the screen was touched at all
	 * @return
	 */
	public boolean screenTouched(){
		return touched;
	}
	
	/**
	 * true if screen has been touched longer than for a tap
	 * @return
	 */
	public boolean screenPressed(){
		return (touched && prevTouched) && (Math.abs((new Date().getTime()) - lastLiftTime) > pressInterval);
	}
	
	/**
	 * has the user just released their finger?
	 * @return
	 */
	public boolean touchReleased(){
		return (!touched && prevTouched);
	}
	
	public Vector getScreenSize(){
		return ScreenSize;
	}
	
	public void setScreenSize(int width, int height){
		ScreenSize = new Vector(width, height);
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	
	public void resetTimeStep(){
		prevFrameTime = (new Date()).getTime();
		deltaTime = 0;
	}
	
	/**
	 * @return time between current frame and last frame (in seconds)
	 */
	public float deltaTime(){
		return deltaTime/1000f;
	}
	
	//CAMERA METHODS
	public Point GetCameraPosition(){
		return cameraPosition;
	}
	
	public void SetCameraPosition(float x, float y){
		cameraPosition.x = x;
		cameraPosition.y = y;
	}
	
	public void SetCameraPosition(Point pos){
		cameraPosition = pos;
	}
	
	public void MoveCamera(Vector displacement){
		cameraPosition = cameraPosition.translate(displacement);
	}
	
	public void MoveCamera(float x, float y){
		MoveCamera(new Vector(x,y));
	}
	
	public void PlaySound(String name){
		streamTable.put(name, soundPool.play(soundTable.get(name), volume, volume, 1, 0, 1f));
	}
	
	public void LoopSound(String name){
		streamTable.put(name, soundPool.play(soundTable.get(name), volume, volume, 1, -1, 1f));
	}
	
	public void StopSound(String name){
		soundPool.stop(streamTable.get(name));
	}
	
	public void LoadSound(int resID, String name){
		soundTable.put(name, soundPool.load(context, resID, 1));
	}
	
	public void SetVolume(float volume){
		this.volume = volume;
	}
	
	/**
	 * give the GameState references to the user settings
	 * @param settings
	 * @param editor
	 */
	private void UpdateSettingReference(SharedPreferences settings){
		this.settings = settings;
	}
	
	/**
	 * Use the user settings to update values in the GameState
	 */
	public void UpdateSettingValues(){
		volume = settings.getFloat("Volume", 0.5f);
		
		switch (settings.getInt("LaserMode", 0)){
		case 0:
			laserMode = LaserControl.Shake;
			break;
		case 1:
			laserMode = LaserControl.TouchShip;
			break;
		case 2:
			laserMode = LaserControl.TouchScreen;
			break;
		}
		
		switch (settings.getInt("ShipMode", 0)){
		case 0:
			shipMode = ShipControl.Target;
			break;
		case 1:
			shipMode = ShipControl.Joystick;
			break;
		case 2:
			shipMode = ShipControl.Swipe;
			break;
		}
	}
	
	public void UpdateSettings(SharedPreferences settings){
		UpdateSettingReference(settings);
		UpdateSettingValues();
	}
	
	public LaserControl getLaserMode(){
		return laserMode;
	}
	
	public ShipControl getShipMode(){
		return shipMode;
	}
	
	/**
	 * how the game state receives the latest information from bluetooth (via the bluetooth service)
	 * @param message
	 */
	/*
	public void bluetoothInput(String message){
		lastBluetoothMessage = message;
		isMsgRead = false;
	}
	*/
	public void bluetoothInput(int[] message){
		lastBluetoothMessage = message;
		isMsgRead = false;
	}
	
	/**
	 * reads the latest bluetooth message, if it hasn't already been read
	 */
	public int[] getLastBluetooth(){
		if (isMsgRead){
			return null;
		}
		else{
			isMsgRead = true;
			return lastBluetoothMessage;
		}
	}
	/*
	public String getLastBluetooth(){
		if (isMsgRead){
			return null;
		}
		else{
			isMsgRead = true;
			return lastBluetoothMessage;
		}
	}
	*/
}
