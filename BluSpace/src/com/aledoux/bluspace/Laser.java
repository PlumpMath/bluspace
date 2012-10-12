package com.aledoux.bluspace;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;

public class Laser extends GameObject {
	public static ArrayList<Laser> ALL_LASERS = new ArrayList<Laser>();
	
	Vector velocity;
	float lifeSpan, lifeCount;
	//CircleSprite sprite;
	AnimatedBitmap sprite;
	int OwnerID; //which player does this belong to?
	
	public Laser(Point pos, Vector velocity, int OwnerID){
		this.pos = pos;
		this.velocity = velocity;
		this.lifeSpan = 1; //max length of life in seconds
		this.lifeCount = 0; //total time alive
		this.sprite = new AnimatedBitmap(R.drawable.redlasersml, 1, 3, 0.1f, AnimatedBitmap.Type.BACK_AND_FORTH);
		this.OwnerID = OwnerID;
	}
	
	public void update(){
		//update life count
		lifeCount += GameState.State().deltaTime();
		
		//move the laser
		pos = pos.translate(velocity.mult(GameState.State().deltaTime()));
		
		//animate the laser's sprite
		sprite.animate(GameState.State().deltaTime());
		
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
