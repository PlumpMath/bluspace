package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;

public class Laser extends GameObject {
	Vector velocity;
	float lifeSpan, lifeCount;
	CircleSprite sprite;
	
	public Laser(Point pos, Vector velocity){
		this.pos = pos;
		this.velocity = velocity;
		this.lifeSpan = 1; //max length of life in seconds
		this.lifeCount = 0; //total time alive
		this.sprite = new CircleSprite(2,Color.argb(255,255,255,255));
	}
	
	public void update(){
		//update life count
		lifeCount += GameState.State().deltaTime();
		
		//move the laser
		pos = pos.move(velocity.mult(GameState.State().deltaTime()));
		
		//if it goes off screen, push it around to the other side
		Vector ss = GameState.State().ScreenSize;
		if (pos.x < 0){
			pos.x = ss.x;
		}
		else if (pos.x > ss.x){
			pos.x = 0;
		}
		if (pos.y < 0){
			pos.y = ss.y;
		}
		else if (pos.y > ss.y){
			pos.y = 0;
		}
		
		//if the laser has existed for too long, destroy it
		if (lifeCount > lifeSpan){
			GameObject.destroy(this);
		}
	}

	@Override
	public void render(Canvas canvas) {
		sprite.draw(canvas,this.pos);
	}

}
