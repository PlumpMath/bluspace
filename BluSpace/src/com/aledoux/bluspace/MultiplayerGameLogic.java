package com.aledoux.bluspace;

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
	
	/**
	 * An initialization function that's called the first time this object is updated
	 * IMPORTANT: do NOT try to replicate this behavior with a constructor - it WON'T work
	 */
	public void OnStart(){
		//create the random space background and put its priority behind everything else
		background = new Background(R.drawable.spaceback_sml,new Point(0,0),-1);

		//create a border for the background (draw on top)
		new Border(background,1000);
		//create player 1 in the middle of the background
		player = new Player(new Point(0,0));
		//create a target reticule for the player
		target = new Target(player);
		
		//create player 2 a little ways away
		opponent = new Opponent(new Point(50,50));
		
		//load sounds
		GameState.State().LoadSound(R.raw.shoot, "shoot");
		GameState.State().LoadSound(R.raw.accelerate, "accelerate");
		GameState.State().LoadSound(R.raw.engine, "engine");
		GameState.State().LoadSound(R.raw.explosion, "explosion");
		GameState.State().LoadSound(R.raw.death, "death");
		
		//bluetooth
		bluetoothCounter = 0.0f;
		bluetoothDelay = 0.1f; //wait nth of a second between bluetooth updates
				
		hasStarted = true;
	}
	
	@Override
	public void update() {
		if (!hasStarted){
			OnStart();
		}
		
		//screen wrap appropriate game objects
		ScreenWrap(player);
		for (Laser l : GameObject.allObjectsOfType(Laser.class)){
			ScreenWrap(l);
		}
		for (Asteroid a : GameObject.allObjectsOfType(Asteroid.class)){
			ScreenWrap(a);
		}
		
		//center camera on player
		GameState.State().SetCameraPosition(player.pos.translate(GameState.State().ScreenSize.div(-2)));
		
		//send a receive bluetooth data
		if (bluetooth != null){
			bluetoothCounter += GameState.State().deltaTime();
			if (bluetoothCounter >= bluetoothDelay){
				//DEBUG: ONLY ONE MESSAGE AT A TIME
				String sendMsg = "";
				
				boolean foundMsg = false;
				for (Laser l : GameObject.allObjectsOfType(Laser.class)){
					if (!foundMsg){
						if (!l.isSent){
							sendMsg += "/LASER:"+l.bluetoothData();
							l.isSent = true;
							foundMsg = true;
						}
					}
				}
				
				if (!foundMsg){
					if (GameObject.StillExists(player)){ //if the player still exists
						sendMsg += "/SHIP:"+player.bluetoothData();
					}
					else{
						sendMsg += "/SHIP:DEAD";
					}
					foundMsg = true;
				}
				
				bluetooth.write(sendMsg.getBytes());
				
				bluetoothCounter = 0.0f;
				
				//OLD VERSION
				/**
				String sendMsg = "";
				
				if (player != null){
					sendMsg += "/SHIP:"+player.bluetoothData();
				}
				else{
					sendMsg += "/SHIP:DEAD";
				}
				
				bluetooth.write(sendMsg.getBytes());
				
				for (Laser l : GameObject.allObjectsOfType(Laser.class)){
					sendMsg = "";
					if (!l.isSent){
						sendMsg += "/LASER:"+l.bluetoothData();
						l.isSent = true;
					}
					bluetooth.write(sendMsg.getBytes());
				}
						
				//bluetooth.write(sendMsg.getBytes());
				
				bluetoothCounter = 0.0f;
				**/
			}
			
			String newMsg = GameState.State().getLastBluetooth();
			if (newMsg != null){
				/**
				String[] msgList = newMsg.split("/");
				
				for (String msg : msgList){
					String[] msgParts = msg.split(":");
					String name = msgParts[0];
					String data = msgParts[1];
					
					if (name.equals("SHIP")){
						opponent.parseBluetoothData(data);
					}
					else if (name.equals("LASER")){
						new Laser(data);
					}
				}
				**/
				String[] msgParts = newMsg.split(":");
				String name = msgParts[0];
				String data = msgParts[1];
				
				if (name.equals("/SHIP") && GameObject.StillExists(opponent)){
					opponent.parseBluetoothData(data);
				}
				else if (name.equals("/LASER")){
					new Laser(data);
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


}
