package com.aledoux.bluspace;

import android.util.Log;

/**
 * this class represents player 2, whose actions are sent over the air by bluetooth
 * @author adamrossledoux
 *
 */
public class Opponent extends Spaceship {
	float goalAngle; //the angle the ship wants to be at
	
	public Opponent(Point pos, int ID) {
		super(pos, ID);
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
	
	public void Die(){
		GameObject.destroy(this);
		new Explosion(this.pos);
	}
	
	public void UpdateState(int[] data){
		pos.x = (float) data[1];
		pos.y = (float) data[2];
		velocity.x = (float) data[3];
		velocity.y = (float) data[4];
		rotationAngle = sprite.setRotation( ((float) data[5]) / 100f );
	}
}
