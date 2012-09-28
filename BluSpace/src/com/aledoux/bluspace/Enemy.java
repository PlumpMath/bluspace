package com.aledoux.bluspace;

import android.graphics.Canvas;

public class Enemy extends GameObject {
	BitmapSprite sprite;
	
	public Enemy(Point pos){
		this.pos = pos;
		this.sprite = new BitmapSprite(R.drawable.spaceship);
	}
	
	@Override
	public void update() {
		for (Laser l : GameObject.allObjectsOfType(Laser.class)){
			if (collision(l.pos)){
				GameObject.destroy(this);
				new Explosion(this.pos);
			}
		}
	}

	@Override
	public void render(Canvas canvas) {
		sprite.draw(canvas,pos);
	}
	
	public boolean collision(Point p){
		if (Point.Distance(pos, p) < sprite.getWidth()/2){
			return true;
		}
		return false;
	}

}
