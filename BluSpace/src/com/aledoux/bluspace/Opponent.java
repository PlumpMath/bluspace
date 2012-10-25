package com.aledoux.bluspace;

import android.util.Log;

/**
 * this class represents player 2, whose actions are sent over the air by bluetooth
 * @author adamrossledoux
 *
 */
public class Opponent extends Spaceship {
	float goalAngle; //the angle the ship wants to be at
	
	public Opponent(Point pos) {
		super(pos);
		
		//player 2
		ID = 2;
	}
	
	@Override
	public void collisions(){
		//no collisions!
	}
	
	public void parseBluetoothData(String data){
		if (data.equals("DEAD")){
			GameObject.destroy(this);
			new Explosion(this.pos);
		}
		else{
			String[] dataSplit = data.split(";");
			
			String posStr = dataSplit[0];
			String velocityStr = dataSplit[1];
			String angleStr = dataSplit[2];
			
			String[] posSplit = posStr.split(",");
			String[] velocitySplit = velocityStr.split(",");
			
			pos.x = Float.parseFloat(posSplit[0]);
			pos.y = Float.parseFloat(posSplit[1]);
			
			velocity.x = Float.parseFloat(velocitySplit[0]);
			velocity.y = Float.parseFloat(velocitySplit[1]);
			
			rotationAngle = sprite.setRotation(Float.parseFloat(angleStr));
		}
	}
}
