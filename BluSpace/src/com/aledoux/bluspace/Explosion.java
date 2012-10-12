package com.aledoux.bluspace;

import android.graphics.Canvas;

public class Explosion extends GameObject {
	AnimatedBitmap sprite;
	
	public Explosion(Point pos){
		this.pos = pos;
		sprite = new AnimatedBitmap(R.drawable.explosions_sml,2,4,0.1f,AnimatedBitmap.Type.ONCE);
		this.renderPriority = 100;
		//play the explosion sound when this is created
		GameState.State().PlaySound("explosion");
	}
	
	@Override
	public void update() {
		boolean animationComplete = sprite.animate(GameState.State().deltaTime()); //animate the sprite
		if (animationComplete){ //when the explosion finishes its animation, destroy it
			GameObject.destroy(this);
		}
	}

	@Override
	public void render(Canvas canvas) {
		sprite.draw(canvas,pos);
	}

}
