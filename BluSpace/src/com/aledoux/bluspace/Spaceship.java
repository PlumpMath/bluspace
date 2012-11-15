package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

/**
 * This class represents a spaceship: either player 1 or player 2
 * @author adamrossledoux
 *
 */
public class Spaceship extends GameObject {
	float speed, shootSpeed; //pixels per second
	float thrustSpeed;
	float minDist;
	BitmapSprite sprite;
	float rotationAngle;
	float rotationSpeed; //degrees per second
	Vector heading; //a unit vector representing the direction the ship is heading
	//Vector velocity; //what's the speed of the spaceship?
	public Point target; //what's the target position for the spaceship?
	float frictionForce; //how strong is friction?
	boolean enginesRunning; //are the engines being used (are we increasing velocity)?
	int ID; //which ship is this?
	
	public Spaceship(int x, int y, int ID){
		this(new Point(x,y), ID);
	}
	
	public Spaceship(Point pos, int ID){
		this.pos = pos;
		speed = 100f; //how many pixels we move per second
		thrustSpeed = 20;
		minDist = 2f; //minimum distance (in pixels) from target
		shootSpeed = speed*4;
		rotationAngle = 0;
		rotationSpeed = 180;
		heading = new Vector(rotationAngle-90); //the negative 90 has to do with something weird in the android coord system
		velocity = new Vector(0,0);
		frictionForce = 15;
		enginesRunning = false;
		
		if (ID == 1){
			this.sprite = new BitmapSprite(R.drawable.spaceship_sml);
		}
		else if (ID == 2){
			this.sprite = new BitmapSprite(R.drawable.ufo_sml);
		}
		
		renderPriority = 1; //raise priority above lasers
		
		this.ID = ID;
	}
	
	public void update(){
		
		shootLasers();
		
		boolean wasEngineRunning = enginesRunning;
		
		controlMovement();	
		
		//ENGINE SOUNDS
		if (!wasEngineRunning && enginesRunning){ //if the engines JUST STARTED
			//fire sound
			//GameState.State().PlaySound("accelerate");
			//turn on engine sound
			GameState.State().LoopSound("engine");
		}
		else if (wasEngineRunning && !enginesRunning){ //if the engines JUST STOPPED
			//turn off the engine sound
			GameState.State().StopSound("engine");
		}
		
		//apply friction
		velocity = velocity.sub(velocity.unit().mult(frictionForce).mult(GameState.State().deltaTime()));
		
		//update position based on velocity
		pos = pos.translate(velocity.mult(GameState.State().deltaTime()));
		
		//collisions
		collisions();
		
		//create exhaust
		int color = 0;
		if (ID == 1){
			color = Color.argb(255, 255, 0, 0);
		}
		else{
			color = Color.argb(255, 0, 255, 0);
		}
		new ExhaustParticle(5,color,pos.translate(heading.mult(-1 * sprite.getHeight() * 0.5f)),velocity.mult(-1));
	}
	
	public void collisions(){
		for (Laser l : GameObject.allObjectsOfType(Laser.class)){
			if (collision(l.pos) && (l.OwnerID != ID)){ //collision with laser that does not belong to this ship
				GameObject.destroy(this);
				GameState.State().PlaySound("death");
				new Explosion(this.pos);
			}
		}
		for (Asteroid a : GameObject.allObjectsOfType(Asteroid.class)){
			if (a.collision(this.pos, Math.min(height(), width())/2)){
				GameObject.destroy(this);
				try{
					GameState.State().StopSound("engine");
				}
				catch(Exception e){
					e.printStackTrace();
				}
				GameState.State().PlaySound("death");
				new Explosion(this.pos);
			}
		}
	}
	
	public void controlMovement(){
		
	}
	
	public void shootLasers(){
		
	}

	@Override
	public void render(Canvas canvas) {		
		sprite.draw(canvas,pos);
	}
	
	/**
	 * Does the point p collide with this ship?
	 * @param p
	 * @return isCollision
	 */
	public boolean collision(Point p){
		boolean isCollision = false;
		
		if (p.x < pos.x + width()/2 && p.x > pos.x - width()/2
				&& p.y < pos.y + height()/2 && p.y > pos.y - width()/2){
			isCollision = true;
		}
		
		return isCollision;
	}
	
	/**
	 * point collision within a certain radius
	 * @param p
	 * @param radius
	 * @return
	 */
	public boolean collision(Point p, float radius){
		return Point.Distance(pos, p) < radius;
	}
	
	public int width(){
		return sprite.getWidth();
	}
	
	public int height(){
		return sprite.getHeight();
	}
}
