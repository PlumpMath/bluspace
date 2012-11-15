package com.aledoux.bluspace;

import java.util.ArrayList;

import android.graphics.Canvas;

/**
 * This abstract game object
 * is a blueprint for anything that can
 * be drawn to the screen AND
 * updated by the game logic
 * 
 * @author adamrossledoux
 *
 */
public abstract class GameObject implements Renderable, Updateable {
	//priorities
	public int updatePriority = 0;//default = LOWEST PRIORITY
	public int renderPriority = 0;//default = LOWEST PRIORITY
	
	public Point pos; //current position of the game object
	
	//for physics objects (INELEGANT HAX)
	public Vector velocity;
	
	public GameObject(){
		RenderObject.getRenderables().add(this);
		LogicObject.getLogic().add(this);
	}
	
	public abstract void update();
	
	public abstract void render(Canvas canvas);
	
	public int updatePriority(){
		return updatePriority; 
	}
	
	public int renderPriority(){
		return renderPriority; 
	}
	
	/**
	 * mark a game object for destruction
	 */
	public static void destroy(GameObject g){
		RenderObject.destroy(g);
		LogicObject.destroy(g);
	}
	
	/**
	 * Get a list of all game objects of a certain type
	 * @param objType
	 * @return
	 */
	public static <T> ArrayList<T> allObjectsOfType(Class<T> objType){
		return LogicObject.allObjectsOfType(objType); //uses the logic object method in the background
	}
	
	public static boolean StillExists(GameObject g){
		return LogicObject.getLogic().contains(g);
	}
}
