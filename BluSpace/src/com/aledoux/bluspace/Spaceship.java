package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

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
	private boolean enginesRunning; //are the engines being used (are we increasing velocity)?
	
	//accelerometer stuff
	float shootTime, shootInterval;
	
	//for swipe movement
	Vector swipe;
	
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
		enginesRunning = false;
		
		this.sprite = new BitmapSprite(R.drawable.spaceship);
		
		renderPriority = 1; //raise priority above lasers
		
		//
		shootTime = 0f;
		shootInterval = 0.25f;
	}
	
	public void update(){
		
		shootLasers();
		
		boolean wasEngineRunning = enginesRunning;
		
		controlMovement();	
		
		
		
		//ENGINGE SOUNDS
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
		for (Laser l : GameObject.allObjectsOfType(Laser.class)){
			if (collision(l.pos) && (l.OwnerID != 1)){ //collision with laser that does not belong to this player
				GameObject.destroy(this);
				GameState.State().PlaySound("death");
				new Explosion(this.pos);
			}
		}
	}
	
	public void controlMovement(){
		switch (GameState.State().getShipMode()){
		case Target:
			//if the screen is tapped
			Point touch = null;
			if (GameState.State().screenTouched()){
				touch = GameState.State().lastTouch.translate(new Vector(GameState.State().GetCameraPosition()));
				//touch position has to be adjusted to be relative to the camera

				if (!collision(touch,75)){
				 	//if the tap is not on the spaceship
					//set this location as the target
					target = touch;
					
					//if the velocity is not too fast
					if (velocity.mag() < 300){
						//and increase the velocity of the spaceship (in the forward direction)
						velocity = velocity.add(heading.mult(thrustSpeed)); //multiplier is a hack TODO
						//play acceleration sound
						//GameState.State().PlaySound("accelerate");
					}
				}
			}
			//if we have a target
			if (target != null){
				//rotate toward target
				rotationAngle = sprite.rotateTowards(new Vector(pos, pos.translate(heading)), new Vector(pos, target), rotationSpeed*GameState.State().deltaTime(), 10);
				//update heading
				heading = new Vector(rotationAngle-90); //the negative 90 has to do with something weird in the android coord system
			}
			break;
		case Joystick:
			target = null;
			/**
			 * This is the new move method that follows the vector drawing idea
			 * @author Sean Wheeler
			 */
			if (GameState.State().screenTouched())
			{
				Vector moveVec = (new Vector(GameState.State().firstTouch,GameState.State().lastTouch));
				Vector from = new Vector(pos, pos.translate(heading));
				Vector to =  new Vector(pos, pos.translate(moveVec));
				rotationAngle = sprite.rotateTowards(from, to, rotationSpeed*GameState.State().deltaTime(), 10);
				heading = new Vector(rotationAngle-90); //the negative 90 has to do with something weird in the android coord system
				//GameState.State().PlaySound("accelerate");
				if (velocity.mag() < 400)
				{
					velocity = velocity.add(moveVec.unit().mult(thrustSpeed));
					enginesRunning = true;
				}
				else{
					enginesRunning = false;
				}
			}
			else{
				enginesRunning = false;
			}
			break;
		case Swipe:
			target = null;
			if (GameState.State().touchReleased()){
				swipe = (new Vector(GameState.State().firstTouch,GameState.State().lastTouch));
				if (velocity.mag() < 400)
				{
					velocity = velocity.add(swipe.mult(thrustSpeed*0.1f)); //multiplier is a HACK TODO
				}
			}
			if (swipe != null){
				//rotate towards heading
				Vector from = new Vector(pos, pos.translate(heading));
				Vector to = new Vector(pos, pos.translate(swipe));
				rotationAngle = sprite.rotateTowards(from, to, rotationSpeed*GameState.State().deltaTime(), 10);
				heading = new Vector(rotationAngle-90); //the negative 90 has to do with something weird in the android coord system
			}
		}
	}
	
	public void shootLasers(){
		//shooting lasers
		shootTime += GameState.State().deltaTime();

		switch (GameState.State().getLaserMode()){
			case Shake:
				//if the phone is shaken by the user
				if (GameState.State().isShaking() && (shootTime > shootInterval)){
					//shoot a laser forward
					new Laser(pos, heading.mult(shootSpeed).add(velocity), 1);
					//play laser sound
					GameState.State().PlaySound("shoot");	
					shootTime = 0f;
				}
				break;
			case TouchShip:
				//if the screen is tapped
				Point touch = null;
				if (GameState.State().screenTapped()){
					touch = GameState.State().lastTouch.translate(new Vector(GameState.State().GetCameraPosition()));
					//touch position has to be adjusted to be relative to the camera
					//if the tap is WITHIN A 75 PIXEL RADIUS of the spaceship
					if (collision(touch,75)){
						//shoot a laser forward
						new Laser(pos, heading.mult(shootSpeed).add(velocity), 1);
						//play laser sound
						GameState.State().PlaySound("shoot");
					}
				}
				break;
			case TouchScreen:
				//if the screen is tapped
				if (GameState.State().screenTapped()){
					//shoot a laser forward
					new Laser(pos, heading.mult(shootSpeed).add(velocity), 1);
					//play laser sound
					GameState.State().PlaySound("shoot");
				}
				break;
		}
	}

	@Override
	public void render(Canvas canvas) {
		//draw "exhaust"
		if (enginesRunning){
			(new CircleSprite(10,Color.argb(255,255,0,0))).draw(canvas, pos.translate(heading.mult(-15)));
		}
		
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
