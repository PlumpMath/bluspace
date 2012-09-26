package com.aledoux.bluspace;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Abstract class for all updateable (game logic) objects
 * @author adamrossledoux
 *
 */
public abstract class LogicObject implements Updateable {
	private static ArrayList<Updateable> ALL_LOGIC; //all the updateable objects in the WHOLE game should be in this list
	private static ArrayList<Updateable> LOGIC_TO_DESTROY; //all updateable objects that need to be destroyed
	
	public int updatePriority = 0; //default = LOWEST PRIORITY
	
	public LogicObject(){
		getLogic().add(this);
	}
	
	public abstract void update();

	public int updatePriority() {
		return updatePriority;
	}
	
	/**
	 * returns a list of all updateable objects in the game
	 * @return
	 */
	public static ArrayList<Updateable> getLogic(){
		if (ALL_LOGIC == null){
			ALL_LOGIC = new ArrayList<Updateable>();
		}
		return ALL_LOGIC;
	}
	
	/**
	 * gets the list of updateable objects to destroy (makes it if it does not exist)
	 * @return
	 */
	private static ArrayList<Updateable> getDestroyList(){
		if (LOGIC_TO_DESTROY == null){
			LOGIC_TO_DESTROY = new ArrayList<Updateable>();
		}
		return LOGIC_TO_DESTROY;
	}
	
	/**
	 * mark an updateable for destruction
	 */
	public static void destroy(Updateable u){
		getDestroyList().add(u);
	}
	
	/**
	 * removes all updateable already marked for destruction from the master updateable list
	 */
	public static void cull(){
		for (Updateable u : getDestroyList()){
			getLogic().remove(u);
		}
		LOGIC_TO_DESTROY = new ArrayList<Updateable>(); //reset the destroy list
	}
	
	/**
	 * returns a queue of all updateable objects, sorted by
	 * the order they need to be updated
	 * @return
	 */
	public static PriorityQueue<Updateable> getUpdateQueue(){
		PriorityQueue<Updateable> UpdateQueue = new PriorityQueue<Updateable>(11,new SortUpdateable());
		UpdateQueue.addAll(getLogic());
		return UpdateQueue;
	}
	
	/**
	 * Get a list of all logic objects of a certain type
	 * @param objType
	 * @return
	 */
	public static <T> ArrayList<T> allObjectsOfType(Class<T> objType){
		ArrayList<T> typeList = new ArrayList<T>();
		
		for (Updateable u : LogicObject.getLogic()){
			if (u.getClass() == objType){
				typeList.add((T) u);
			}
		}
		
		return typeList;
	}

}
