package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;

public class ExhaustParticle extends GameObject {
	private CircleSprite sprite;
	int color;
	float size;
	
	public ExhaustParticle(int size, int color, Point pos, Vector velocity){
		this.pos = pos;
		this.velocity = velocity;
		this.size = ((float)size + (float)(Math.random() * size));
		this.color = color;
		this.sprite = new CircleSprite((int)this.size,color);
	}
	
	@Override
	public void update() {
		pos = pos.translate(velocity.mult(GameState.State().deltaTime()));
		
		size -= 1.5f;
		sprite = new CircleSprite((int)size,color);
		if (size <= 0){
			GameObject.destroy(this);
		}
		
	}

	@Override
	public void render(Canvas canvas) {
		sprite.draw(canvas,pos);
	}
}
