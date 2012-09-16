package com.aledoux.bluspace;

import android.graphics.Canvas;

/**
 * Interface for an object that can be drawn to the screen
 * @author adamrossledoux
 *
 */
public interface Renderable {
	/**
	 * method that draws the object onto the canvas
	 * @param canvas
	 */
	public void render (Canvas canvas);
	
	/**
	 * how far up in the order of rendered objects should this be?
	 */
	public int renderPriority();
}
