package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.util.Log;

public class Enemy extends GameObject {
	BitmapSprite sprite;
	float shootInterval, shootTime;
	
	public Enemy(Point pos){
		this.pos = pos;
		this.sprite = new BitmapSprite(R.drawable.spaceship);
		shootTime = 0f;
		shootInterval = 1.2f;
	}
	
	@Override
	public void update() {
		for (Laser l : GameObject.allObjectsOfType(Laser.class)){
			if (collision(l.pos) && l.OwnerID != 2){
				GameObject.destroy(this);
				new Explosion(this.pos);
			}
		}
		
		shootTime += GameState.State().deltaTime();
		if (shootTime >= shootInterval){
			//Log.i("shoot","shoot");
			new Laser(this.pos,new Vector(0,-400),2);
			shootTime = 0f;
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
