package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Target extends GameObject {
	CircleSprite sprite;
	
	public Target(){
		sprite = new CircleSprite(20,Color.argb(255,255,255,255));
		sprite.setStyle(Paint.Style.STROKE);
	}
	
	@Override
	public void update() {
		if (GameState.State().screenTouched()){
			pos = GameState.State().lastTouch;
		}
	}

	@Override
	public void render(Canvas canvas) {
		//only draw the target if it has a position
		if (pos != null){
			sprite.draw(canvas, pos);
		}
	}

}
