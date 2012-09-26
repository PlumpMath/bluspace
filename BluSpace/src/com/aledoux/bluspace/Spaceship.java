package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;

public class Spaceship extends GameObject {
	float speed, shootSpeed; //pixels per second
	float thrustSpeed;
	float minDist;
	BitmapSprite sprite;
	float rotationAngle;
	float rotationSpeed; //degrees per second
	Vector heading; //a unit vector representing the direction the ship is heading
	Vector velocity; //what's the speed of the spaceship?
	public Point target; //what's the target position for the spaceship?
	float frictionForce; //how strong is friction?
	
	public Spaceship(int x, int y){
		this(new Point(x,y));
	}
	
	public Spaceship(Point pos){
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
		
		this.sprite = new BitmapSprite(R.drawable.spaceship);
		
		renderPriority = 1; //raise priority above lasers
	}
	
	public void update(){
		//if the screen is tapped
		if (GameState.State().screenTapped()){
			Point touch = GameState.State().lastTouch.translate(new Vector(GameState.State().GetCameraPosition()));
			//touch position has to be adjusted to be relative to the camera
			
			//if the tap is WITHIN A 75 PIXEL RADIUS of the spaceship
			if (collision(touch,75)){
				//shoot a laser forward
				new Laser(pos, heading.mult(shootSpeed));
				//play laser sound
				GameState.State().PlaySound("shoot");
			}
			else{ //if the tap is elsewhere on the screen
				//set this location as the target
				target = touch;
				//and increase the velocity of the spaceship (in the forward direction)
				velocity = velocity.add(heading.mult(thrustSpeed));
				//play acceleration sound
				GameState.State().PlaySound("accelerate");
			}
		}
		
		//if we have a target
		if (target != null){
			//rotate toward target
			rotationAngle = sprite.rotateTowards(new Vector(pos, pos.translate(heading)), new Vector(pos, target), rotationSpeed*GameState.State().deltaTime(), 10);
			
			//update heading
			heading = new Vector(rotationAngle-90); //the negative 90 has to do with something weird in the android coord system
		}
		
		//apply friction
		velocity = velocity.sub(velocity.unit().mult(frictionForce).mult(GameState.State().deltaTime()));
		
		//update position based on velocity
		pos = pos.translate(velocity.mult(GameState.State().deltaTime()));
		
		/**
		//if the ship goes of the screen wrap it around
		Vector ss = GameState.State().getScreenSize();
		if (pos.x < 0 || pos.x > ss.x){
			pos.x = Math.abs(pos.x - ss.x);
		}
		if (pos.y < 0 || pos.y > ss.y){
			pos.y = Math.abs(pos.y - ss.y);
		}
		**/
	}

	@Override
	public void render(Canvas canvas) {
		//draw "exhaust"
		if (GameState.State().screenTapped()){
			(new CircleSprite(10,Color.argb(255,255,0,0))).draw(canvas, pos.translate(heading.mult(-15)));
		}
		
		sprite.draw(canvas,pos);
	}
	
	/**
	 * Does the point p collide with this ship?
	 * @param p
	 * @return isCollision
	 */
	public boolean collision(Vector p){
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
