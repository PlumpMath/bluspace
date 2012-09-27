package com.aledoux.bluspace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class AnimatedBitmap extends Sprite {
	private Bitmap sourceBitmap;
	private int rows, cols, numFrames, frame;
	private float time, animationSpeed;
	
	public AnimatedBitmap(int id, int rows, int cols, float animationSpeed){
		this.sourceBitmap = BitmapFactory.decodeResource(GameState.State().context.getResources(), id);
		this.rows = rows;
		this.cols = cols;
		this.numFrames = rows * cols;
		this.frame = 0;
		this.animationSpeed = animationSpeed;
		this.time = 0f;
	}
	
	public void animate(float deltaTime){
		time += deltaTime;
		if (time >= animationSpeed){
			frame++;
			
			if (frame >= numFrames){
				frame = 0;
			}
			
			time = 0f;
		}
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
