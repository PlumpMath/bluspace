package com.aledoux.bluspace;

import android.graphics.Canvas;

public class Explosion extends GameObject {
	AnimatedBitmap sprite;
	float time, lifeTime;
	
	public Explosion(Point pos){
		this.pos = pos;
		sprite = new AnimatedBitmap(R.drawable.explosions_sml,2,4,0.1f);
		this.renderPriority = 100;
		this.lifeTime = 1.5f; //lives 1.5 seconds
		//play the explosion sound when this is created
		GameState.State().PlaySound("explosion");
	}
	
	@Override
	public void update() {
		time += GameState.State().deltaTime();
		if (time > lifeTime){
			GameObject.destroy(this);
		}
	}

	@Override
	public void render(Canvas canvas) {
		sprite.animate(GameState.State().deltaTime());
		sprite.draw(canvas,pos);
	}

}
