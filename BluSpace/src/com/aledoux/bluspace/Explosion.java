package com.aledoux.bluspace;

import android.graphics.Canvas;

public class Explosion extends GameObject {
	AnimatedBitmap sprite;
	
	public Explosion(Point pos){
		this.pos = pos;
		sprite = new AnimatedBitmap(R.drawable.explosions_sml,2,4,0.1f);
		this.renderPriority = 100;
	}
	
	@Override
	public void update() {
		//nothing right now
	}

	@Override
	public void render(Canvas canvas) {
		sprite.animate(GameState.State().deltaTime());
		sprite.draw(canvas,pos);
	}

}
