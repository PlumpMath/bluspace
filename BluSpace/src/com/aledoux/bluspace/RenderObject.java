package com.aledoux.bluspace;

import java.util.ArrayList;
import java.util.PriorityQueue;

import android.graphics.Canvas;

/**
 * Abstract class for all renderable objects in the game
 * @author adamrossledoux
 *
 */
public abstract class RenderObject implements Renderable {
	private static ArrayList<Renderable> ALL_RENDERABLES; //all the renderable objects in the WHOLE game should be in this list
	private static ArrayList<Renderable> RENDERABLES_TO_DESTROY; //all renderable objects that need to be destroyed
	
	public int renderPriority = 0; //default = LOWEST PRIORITY
	
	public Point pos;
	
	public RenderObject(){
		getRenderables().add(this);
	}
	
	public abstract void render(Canvas canvas);

	public int renderPriority(){
		return renderPriority;
	}
	
	/**
	 * returns a list of all renderable objects in the game
	 * @return
	 */
	public static ArrayList<Renderable> getRenderables(){
		if (ALL_RENDERABLES == null){
			ALL_RENDERABLES = new ArrayList<Renderable>();
		}
		return ALL_RENDERABLES;
	}
	
	/**
	 * gets the list of renderable objects to destroy (makes it if it does not exist)
	 * @return
	 */
	private static ArrayList<Renderable> getDestroyList(){
		if (RENDERABLES_TO_DESTROY == null){
			RENDERABLES_TO_DESTROY = new ArrayList<Renderable>();
		}
		return RENDERABLES_TO_DESTROY;
	}
	
	/**
	 * mark a renderable for destruction
	 */
	public static void destroy(Renderable r){
		getDestroyList().add(r);
	}
	
	/**
	 * removes all renderables already marked for destruction from the master renderable list
	 */
	public static void cull(){
		for (Renderable r : getDestroyList()){
			getRenderables().remove(r);
		}
		RENDERABLES_TO_DESTROY = new ArrayList<Renderable>(); //reset the destroy list
	}
	
	/**
	 * returns a queue of all renderable objects, sorted by
	 * the order they need to be rendered
	 * @return
	 */
	public static PriorityQueue<Renderable> getRenderQueue(){
		PriorityQueue<Renderable> RenderQueue = new PriorityQueue<Renderable>(11,new SortRenderable());
		RenderQueue.addAll(getRenderables());
		return RenderQueue;
	}

}
