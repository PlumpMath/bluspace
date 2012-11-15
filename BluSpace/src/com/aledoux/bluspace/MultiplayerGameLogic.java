package com.aledoux.bluspace;

import java.nio.ByteBuffer;

import android.util.Log;

/**
 * The multiplayer game logic for BluSpace
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
public class MultiplayerGameLogic extends LogicObject {
	//bluetooth
	private BluetoothChatService bluetooth;
	private float bluetoothCounter, bluetoothDelay;
	
	private boolean hasStarted = false;
	
	private Player player;
	private Opponent opponent;
	private Target target;
	private Background background;
	
	public int backgroundCase = -1;
	public int asteroidCase = -1;
	public int myID = 1;
	public int opponentID = 2;
	Point myPos, oPos;
	private boolean isReady = false; //ready to start
	
	private float respawnCount, respawnTime;
	
	/**
	 * An initialization function that's called the first time this object is updated
	 * IMPORTANT: do NOT try to replicate this behavior with a constructor - it WON'T work
	 */
	public void OnStart(){
		if (myID == 1){
			myPos = new Point(-300,300);
			oPos = new Point(300,-300);
		}
		else{
			myPos = new Point(300,-300);
			oPos = new Point(-300,300);
		}
		
		//create the random space background and put its priority behind everything else
		switch (backgroundCase){
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
		initAsteroids(asteroidCase);

		//create a border for the background (draw on top)
		new Border(background,1000);
		//create player 1 in the middle of the background
		player = new Player(myPos, myID);
		//create a target reticule for the player
		target = new Target(player);
		
		//create player 2 a little ways away
		opponent = new Opponent(oPos, opponentID);
		
		//load sounds
		GameState.State().LoadSound(R.raw.shoot, "shoot");
		GameState.State().LoadSound(R.raw.accelerate, "accelerate");
		GameState.State().LoadSound(R.raw.engine, "engine");
		GameState.State().LoadSound(R.raw.explosion, "explosion");
		GameState.State().LoadSound(R.raw.death, "death");
		
		//bluetooth
		bluetoothCounter = 0.0f;
		bluetoothDelay = 1f/20f; //bluetooth update 1f/(X times per second)

		respawnCount = 0.0f;
		respawnTime = 4f;
		
		hasStarted = true;
	}
	
	@Override
	public void update() {
		if (!hasStarted && isReady){
			OnStart();
		}
		
		if (hasStarted){
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
			
			//send a receive bluetooth data
			if (bluetooth != null){
				bluetoothCounter += GameState.State().deltaTime();
				if (bluetoothCounter >= bluetoothDelay){
					//NEW VERSION
					//TESTING
					ByteBuffer bb = ByteBuffer.allocate(24);
					int[] msgArray = {-1,-1,-1,-1,-1,-1}; //default: error code
					boolean foundMsg = false;
					
					for (Laser l : GameObject.allObjectsOfType(Laser.class)){
						if (!foundMsg){
							if (!l.isSent){
								msgArray = l.bluetoothData();
								l.isSent = true;
								foundMsg = true;
							}
						}
					}
					
					if (!foundMsg){
						if (GameObject.StillExists(player)){ //if the player still exists
							msgArray = player.bluetoothData();
						}
						else{
							int[] dead = {0,0,0,0,0,0}; //dead player
							msgArray = dead; 
						}
						foundMsg = true;
					}
					
					bb.asIntBuffer().put(msgArray);
					bluetooth.write(bb.array());
					
					bluetoothCounter = 0.0f;
				}
				
				
				int[] newMsg = GameState.State().getLastBluetooth();
				if (newMsg != null){
					//NEW VERSION
					if (newMsg[0] == 0 && GameObject.StillExists(opponent)){
						opponent.Die();
					}
					else if (newMsg[0] == 1 && GameObject.StillExists(opponent)){
						opponent.UpdateState(newMsg);
					}
					else if (newMsg[0] == 1 && !GameObject.StillExists(opponent)){
						opponent = new Opponent(oPos, opponentID); //reborn opponent
					}
					else if (newMsg[0] == 2){
						new Laser(newMsg,opponent.ID);
					}
				}
			}
			
			if (!GameObject.StillExists(player)){
				respawnCount += GameState.State().deltaTime();
				if (respawnCount > respawnTime){
					player = new Player(myPos, myID);
					respawnCount = 0.0f;
				}
			}
		}
		else{ //trying to start the game
			if (GameState.State().isHost){
				int[] newMsg = GameState.State().getLastBluetooth();
				if (newMsg != null){
					isReady = true;
				}
				else{
					//send start data to opponent
					ByteBuffer bb = ByteBuffer.allocate(24);
					int[] msgArray = {3,opponentID,myID,backgroundCase,asteroidCase,0};
					bb.asIntBuffer().put(msgArray);
					bluetooth.write(bb.array());
				}
			}
			else{
				int[] newMsg = GameState.State().getLastBluetooth();
				if (newMsg != null){
					//NEW VERSION
					if (newMsg[0] == 3){
						myID = newMsg[1];
						opponentID = newMsg[2];
						backgroundCase = newMsg[3];
						asteroidCase = newMsg[4];
						isReady = true;
					}
				}
			}
		}
	}
	
	public void setBluetooth(BluetoothChatService bluetooth){
		this.bluetooth = bluetooth;
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
			case 3:
				//no asteroids :(
				break;
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


}
