package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectSprite extends Sprite {
	float width, height;
	Paint paint;
	
	public RectSprite(float width, float height, int color){
		this.width = width;
		this.height = height;
		paint = new Paint();
		paint.setColor(color);
	}
	
	@Override
	public void draw(Canvas canvas, Point pos) {
		canvas.drawRect((int)(pos.x - width/2),(int)(pos.y - height/2),(int)(pos.x + width/2),(int)(pos.y + height/2),paint);
	}

}
