package com.aledoux.bluspace;

import java.util.Date;
import java.util.PriorityQueue;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;

/**
 * Stores all the logic for the game
 * draws it onto the canvas,
 * and recieves input from the screen.
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
	//size of the screen
	Vector ScreenSize;
	//camera
	public Camera camera;
	//context
	public Context context;
	
	/**
	 * TOUCH VARIABLES
	 */
	//have we received a touch input this frame? last frame
	private boolean touched, prevTouched;
	//the last location that was touched
	public Vector lastTouch;
	//the last times the screen was touched
	public long touchTime, prevTouchTime;
	//the time delay needed for something to count as a distinct tap OR a long press (in milliseconds)
	public long tapInterval, pressInterval;
	//when was the last time a touch was lifted?
	public long lastLiftTime;
	
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
		
		//null values
		lastTouch = null;
		ScreenSize = null;
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
		//update all updateable game logic objects
		PriorityQueue<Updateable> queue = LogicObject.getUpdateQueue();
		while (!queue.isEmpty()){
			queue.poll().update();
		}
		
		//destroy all game objects, render objects, and logic objects that need to be destroyed
		GameState.cull();
		
		//update touch status variables
		updateTouchStatus();
		
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
		touched = false; //set the current state's touched variable to false (innocent until proven guilty)
	}

	
	/**
	 * Gets touch input from outside
	 * @param x
	 * @param y
	 */
	public void touchInput(int x, int y){
		//update record of last touch position
		lastTouch = new Vector(x,y);
		//note that we've been touched
		touched = true;
		//note time we've been touched
		touchTime = new Date().getTime();
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
}
