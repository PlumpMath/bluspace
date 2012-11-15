package com.aledoux.bluspace;

/**
 * represents player 1, which is controlled by the phone
 * @author adamrossledoux
 *
 */
public class Player extends Spaceship {
	//accelerometer stuff
	float shootTime, shootInterval;	
	//for swipe movement
	Vector swipe;
	
	public Player(Point pos, int ID) {
		super(pos, ID);
		
		shootTime = 0f;
		shootInterval = 0.25f;
	}
	
	@Override
	public void controlMovement(){
		switch (GameState.State().getShipMode()){
		case Target:
			enginesRunning = false;
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
					enginesRunning = true;
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
				velocity = velocity.add(swipe.mult(thrustSpeed*0.1f)); //multiplier is a HACK TODO
				//clamp velocity
				if (velocity.mag() > 200){
					velocity.mult(400f / velocity.mag());
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

	@Override
	public void shootLasers(){
		//shooting lasers
		shootTime += GameState.State().deltaTime();

		/*
		int count = 0;
		for (Laser l : GameObject.allObjectsOfType(Laser.class)){
			if (l.OwnerID == ID){
				count++;
			}
		}
		
		if (count <= 0){ //DEBUG: ONLY ALLOW ONE SHOT AT A TIME
		*/
			switch (GameState.State().getLaserMode()){
				case Shake:
					//if the phone is shaken by the user
					if (GameState.State().isShaking() && (shootTime > shootInterval)){
						//shoot a laser forward
						new Laser(pos, heading.mult(shootSpeed).add(velocity), ID, true);
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
							new Laser(pos, heading.mult(shootSpeed).add(velocity), ID, true);
							//play laser sound
							GameState.State().PlaySound("shoot");
						}
					}
					break;
				case TouchScreen:
					//if the screen is tapped
					if (GameState.State().screenTapped()){
						//shoot a laser forward
						new Laser(pos, heading.mult(shootSpeed).add(velocity), ID, true);
						//play laser sound
						GameState.State().PlaySound("shoot");
					}
					break;
			}
		}
	//}
	
	/*
	public String bluetoothData(){
		return pos.x + "," + pos.y + ";" + velocity.x + "," + velocity.y + ";" + rotationAngle;
	}
	*/
	
	public int[] bluetoothData(){
		int[] data = {1,(int)pos.x,(int)pos.y,(int)velocity.x,(int)velocity.y,(int)(100*rotationAngle)};
		return data;
	}
}
