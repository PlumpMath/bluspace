package com.aledoux.bluspace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class AnimatedBitmap extends Sprite {
	private Bitmap sourceBitmap;
	private int rows, cols, numFrames, frame;
	private int direction; //direction of animation (+1 = forward, -1 = backward)
	private float time, animationSpeed;
	public static enum Type {ONCE, LOOP, BACK_AND_FORTH}; //animation types
	private Type animMode; //what mode of animation are we using?
	
	/**
	 * constructor
	 * @param id
	 * @param rows
	 * @param cols
	 * @param animationSpeed
	 * @param animMode
	 */
	public AnimatedBitmap(int id, int rows, int cols, float animationSpeed, Type animMode){
		this.sourceBitmap = BitmapFactory.decodeResource(GameState.State().context.getResources(), id);
		this.rows = rows;
		this.cols = cols;
		this.numFrames = rows * cols;
		this.frame = 0;
		this.animationSpeed = animationSpeed;
		this.time = 0f;
		this.animMode = animMode;
		this.direction = 1;
	}
	
	/**
	 * by default the animation loops
	 * @param id
	 * @param rows
	 * @param cols
	 * @param animationSpeed
	 */
	public AnimatedBitmap(int id, int rows, int cols, float animationSpeed){
		this(id,rows,cols,animationSpeed,Type.LOOP);
	}
	
	/**
	 * Animates the frame, returns True if it reaches the end of the animation (either end if it's "back and forth")
	 * @param deltaTime
	 * @return
	 */
	public boolean animate(float deltaTime){
		boolean isEnd = false;
		
		time += deltaTime;
		if (time >= animationSpeed){
			frame += direction;
			
			if (frame >= numFrames || frame < 0){
				isEnd = true;
				switch (animMode){
				case ONCE:
					frame = numFrames - 1; //stay on final frame
					break;
				case LOOP:
					frame = 0; //return to beginning
					break;
				case BACK_AND_FORTH:
					direction = -direction; //switch direction
					frame += direction; //get back into the legal animation frames
					break;
				}
			}
			
			time = 0f;
		}
		
		return isEnd;
	}
	
	@Override
	public void draw(Canvas canvas, Point pos) {
		int row = (frame/cols) % rows;
		int col = frame % cols;
		canvas.drawBitmap(
				sourceBitmap, 
				new Rect(col*getWidth(),row*getHeight(),(col*getWidth())+getWidth(),(row*getHeight())+getHeight()), 
				new Rect((int)(pos.x - getWidth()/2),(int)(pos.y - getHeight()/2), (int)(pos.x + getWidth()/2), (int)(pos.y + getHeight()/2)), 
				null);
	}
	
	public int getWidth(){
		return sourceBitmap.getWidth() / cols;
	}
	
	public int getHeight(){
		return sourceBitmap.getHeight() / rows;
	}

}
