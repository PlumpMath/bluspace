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
	
	//bluetooth
	public boolean isSent;
	
	public Laser(Point pos, Vector velocity, int OwnerID, boolean shouldSend){
		this.pos = pos;
		this.velocity = velocity;
		this.lifeSpan = 1; //max length of life in seconds
		this.lifeCount = 0; //total time alive
		this.sprite = new AnimatedBitmap(R.drawable.redlasersml, 1, 3, 0.1f, AnimatedBitmap.Type.BACK_AND_FORTH);
		this.OwnerID = OwnerID;
		
		//bluetooth
		isSent = !shouldSend; //if we should send the laser across bluetooth, mark it as NOT sent
	}
	
	public Laser(String data) { //create a laser from string data (usually bluetooth)
		String[] parts = data.split(";");
		String[] pos = parts[0].split(",");
		String[] velocity = parts[1].split(",");
		
		this.pos = new Point(Float.parseFloat(pos[0]),Float.parseFloat(pos[1]));
		this.velocity = new Vector(Float.parseFloat(velocity[0]),Float.parseFloat(velocity[1]));
		this.lifeSpan = 1;
		this.lifeCount = 0;
		this.sprite = new AnimatedBitmap(R.drawable.redlasersml, 1, 3, 0.1f, AnimatedBitmap.Type.BACK_AND_FORTH);
		this.OwnerID = 2;
		isSent = true;
	}
	
	public Laser(int[] data){
		this(new Point((float)data[1],(float)data[2]), new Vector((float)data[3],(float)data[4]),2,false);
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
	
	/*
	public String bluetoothData(){
		return pos.x + "," + pos.y + ";" + velocity.x + "," + velocity.y;
	}
	*/
	
	public int[] bluetoothData(){
		int[] data = {2,(int)pos.x,(int)pos.y,(int)velocity.x,(int)velocity.y,0};
		return data;
	}
}
