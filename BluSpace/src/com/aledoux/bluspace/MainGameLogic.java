package com.aledoux.bluspace;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

/**
 * The main game logic for BluSpace
 * is stored here. It is a Logic Object
 * and Updateable which means its "update" method is 
 * run every frame by the GameState.
 * 
 * NOTE: a lot of the actual game logic will be housed
 * in the game objects this master class creates
 * 
 * @author adamrossledoux
 *
 */
public class MainGameLogic extends LogicObject {
	private boolean hasStarted = false;
	
	private Player player;
	private Target target;
	private Background background;
	
	private float respawnCount, respawnTime;
	
	/**
	 * An initialization function that's called the first time this object is updated
	 * IMPORTANT: do NOT try to replicate this behavior with a constructor - it WON'T work
	 */
	public void OnStart(){
		//create the random space background and put its priority behind everything else
		switch ((int) (Math.random() * 3)){
			case 0:
				background = new Background(R.drawable.spaceback_med,new Point(0,0),-1);
				break;
			case 1:
				background = new Background(R.drawable.spaceback_asteroids,new Point(0,0),-1);
				break;
			case 2:
				background = new Background(R.drawable.spaceback_nebula,new Point(0,0),-1);
				break;
		}
		//generate asteroids
		initAsteroids((int)(Math.random()*3));
		//create a border for the background (draw on top)
		new Border(background,1000);
		//create the player
		player = new Player(new Point(-300,300), 1);
		//create a target reticule for the player
		target = new Target(player);
		
		//load sounds
		GameState.State().LoadSound(R.raw.shoot, "shoot");
		GameState.State().LoadSound(R.raw.accelerate, "accelerate");
		GameState.State().LoadSound(R.raw.engine, "engine");
		GameState.State().LoadSound(R.raw.explosion, "explosion");
		GameState.State().LoadSound(R.raw.death, "death");
		
		hasStarted = true;
		
		respawnCount = 0.0f;
		respawnTime = 4f;
	}
	
	@Override
	public void update() {
		if (!hasStarted){
			OnStart();
		}
		
		//screen wrap appropriate game objects
		ScreenBounce(player);
		for (Laser l : GameObject.allObjectsOfType(Laser.class)){
			ScreenBounce(l);
		}
		for (Asteroid a : GameObject.allObjectsOfType(Asteroid.class)){
			ScreenBounce(a);
		}
		
		//center camera on player
		GameState.State().SetCameraPosition(player.pos.translate(GameState.State().ScreenSize.div(-2)));
		
		if (!GameObject.StillExists(player)){
			respawnCount += GameState.State().deltaTime();
			if (respawnCount > respawnTime){
				player = new Player(new Point(-300,300), 1);
				respawnCount = 0.0f;
			}
		}
	}
	
	/**
	 * wraps a game object around to the other side of the screen if it goes past the edge of the background
	 * @param o
	 */
	public void ScreenWrap(GameObject o){
		if (o.pos.x > (background.pos.x + background.GetWidth()/2)){
			o.pos.x -= background.GetWidth();
		}
		else if (o.pos.x < (background.pos.x - background.GetWidth()/2)){
			o.pos.x += background.GetWidth();
		}
		if (o.pos.y > (background.pos.y + background.GetHeight()/2)){
			o.pos.y -= background.GetHeight();
		}
		else if (o.pos.y < (background.pos.y - background.GetHeight()/2)){
			o.pos.y += background.GetHeight();
		}
	}
	
	public void ScreenBounce(GameObject o){
		if (o.pos.x > (background.pos.x + background.GetWidth()/2) || o.pos.x < (background.pos.x - background.GetWidth()/2)){
			o.velocity.x *= -1;
			if (o.pos.x > (background.pos.x + background.GetWidth()/2)){
				o.pos.x = background.pos.x + background.GetWidth()/2;
			}
			else{
				o.pos.x = background.pos.x - background.GetWidth()/2;
			}
		}
		if (o.pos.y > (background.pos.y + background.GetHeight()/2) || o.pos.y < (background.pos.y - background.GetHeight()/2)){
			o.velocity.y *= -1;
			if (o.pos.y > (background.pos.y + background.GetHeight()/2)){
				o.pos.y = background.pos.y + background.GetHeight()/2;
			}
			else{
				o.pos.y = background.pos.y - background.GetHeight()/2;
			}
		}
	}
	
	public void initAsteroids(int c){
		switch (c){
			case 0:
				new Asteroid(Asteroid.Size.BIG, new Point(200,200), new Vector(50f,50f));
				new Asteroid(Asteroid.Size.MED, new Point(-100,-100), new Vector(50f,-50f));
				new Asteroid(Asteroid.Size.BIG, new Point(-200,230), new Vector(-50f,50f));
				new Asteroid(Asteroid.Size.BIG, new Point(80,-250), new Vector(-50f,-50f));
				new Asteroid(Asteroid.Size.MED, new Point(100,100), new Vector(-50f,50f));
				break;
			case 1:
				new Asteroid(Asteroid.Size.BIG, new Point(200,200), new Vector(50f,50f));
				new Asteroid(Asteroid.Size.MED, new Point(100,100), new Vector(50f,-50f));
				new Asteroid(Asteroid.Size.BIG, new Point(-200,-10), new Vector(-50f,50f));
				new Asteroid(Asteroid.Size.MED, new Point(-80,250), new Vector(-50f,-50f));
				new Asteroid(Asteroid.Size.MED, new Point(-100,100), new Vector(-50f,50f));
				break;
			case 2:
				new Asteroid(Asteroid.Size.BIG, new Point(50,200), new Vector(50f,50f));
				new Asteroid(Asteroid.Size.MED, new Point(100,220), new Vector(50f,-50f));
				new Asteroid(Asteroid.Size.BIG, new Point(-100,-70), new Vector(-50f,-50f));
				new Asteroid(Asteroid.Size.MED, new Point(-80,150), new Vector(-50f,-50f));
				new Asteroid(Asteroid.Size.MED, new Point(-120,100), new Vector(50f,50f));
				new Asteroid(Asteroid.Size.BIG, new Point(-30,60), new Vector(-50f,50f));
				break;
		}
	}

}
