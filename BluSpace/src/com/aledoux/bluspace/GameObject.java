package com.aledoux.bluspace;

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
	
	public Vector pos; //current position of the game object
	
	public GameObject(){
		//getGameObjects().add(this); //always add new game objects to the global game object list
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
		//getDestroyList().add(this);
		RenderObject.destroy(g);
		LogicObject.destroy(g);
	}
}
