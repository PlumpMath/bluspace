package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Sprite for drawing circles on the screen
 * 
 * @author adamrossledoux
 *
 */
public class CircleSprite extends Sprite {
	private int radius;
	private Paint paint;
	
	public CircleSprite(int radius, int color){
		this.radius = radius;
		this.paint = new Paint();
		this.paint.setColor(color);
	}
	
	public CircleSprite(){ //auto circle - mainly for debugging
		this(3, Color.argb(255, 255, 255, 255));
	}
	
	public void setStyle(Paint.Style style){
		paint.setStyle(style);
	}

	@Override
	public void draw(Canvas canvas, Vector pos) {
		canvas.drawCircle(pos.x, pos.y, radius, paint);
	}
}
