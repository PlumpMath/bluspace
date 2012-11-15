package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * creates a border for an arena
 * @author adamrossledoux
 *
 */
public class Border extends RenderObject {
	RectSprite top, bottom, left, right;
	float height, width;
	
	public Border(Background b, int renderPriority){
		this.pos = b.pos;
		this.height = b.GetHeight();
		this.width = b.GetWidth();
		
		int borderColor = Color.argb(255,255,255,255); //right now the color of these borders is hardcoded
		top = new RectSprite(b.GetWidth()*2,b.GetHeight(),borderColor);
		bottom = new RectSprite(b.GetWidth()*2,b.GetHeight(),borderColor);
		left = new RectSprite(b.GetWidth(),b.GetHeight(),borderColor);
		right = new RectSprite(b.GetWidth(),b.GetHeight(),borderColor);
		
		this.renderPriority = renderPriority;
	}
	
	@Override
	public void render(Canvas canvas) {
		top.draw(canvas,new Point(pos.x, pos.y - GetHeight()));
		bottom.draw(canvas,new Point(pos.x, pos.y + GetHeight()));
		left.draw(canvas,new Point(pos.x - GetWidth(),pos.y));
		right.draw(canvas,new Point(pos.x + GetWidth(),pos.y));
	}
	
	public float GetHeight(){
		return height;
	}
	
	public float GetWidth(){
		return width;
	}

}
