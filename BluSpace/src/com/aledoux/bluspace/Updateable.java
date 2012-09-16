package com.aledoux.bluspace;

/**
 * Any object that can be updated
 * @author adamrossledoux
 *
 */
public interface Updateable {
	/**
	 * method that updates the status of the object
	 */
	public void update();
	
	/**
	 * how far up in the list of logical updates should this be?
	 */
	public int updatePriority();
}
