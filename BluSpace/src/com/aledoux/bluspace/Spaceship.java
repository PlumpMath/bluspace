package com.aledoux.bluspace;

import android.graphics.Canvas;

public class Spaceship extends GameObject {
	float speed, shootSpeed;
	float minDist;
	BitmapSprite sprite;
	
	public Spaceship(int x, int y){
		this(new Vector(x,y));
	}
	
	public Spaceship(Vector pos){
		this.pos = pos;
		speed = 100f; //how many pixels we move per second
		minDist = 2f; //minimum distance (in pixels) from target
		shootSpeed = speed*4;
		this.sprite = new BitmapSprite(R.drawable.spaceship);
		
		renderPriority = 1; //raise priority above lasers
	}
	
	public void update(){
		if (GameState.State().lastTouch != null){
			Vector target = GameState.State().lastTouch;
			
			Vector normDist = (target.sub(pos)).div(Vector.Distance(pos, target));
			
			if (Vector.Distance(pos,target) > minDist){
				//move the spaceship towards its target
				pos = pos.add((normDist.mult(speed)).mult(GameState.State().deltaTimeSec()));
			}
			
			//shoot lasers toward target if the screen has been tapped
			if (GameState.State().screenTapped()){
				new Laser(pos, normDist.mult(shootSpeed));
			}
		}
		
	}

	@Override
	public void render(Canvas canvas) {
		sprite.draw(canvas,pos);
	}
}
