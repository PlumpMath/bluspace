package com.aledoux.bluspace;

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
	
	private Spaceship player;
	private Target target;
	private Background background;
	
	/**
	 * An initialization function that's called the first time this object is updated
	 * IMPORTANT: do NOT try to replicate this behavior with a constructor - it WON'T work
	 */
	public void OnStart(){
		//create the random space background and put its priority behind everything else
		switch ((int) (Math.random() * 3)){
			case 0:
				background = new Background(R.drawable.spaceback_sml,new Point(0,0),-1);
				break;
			case 1:
				background = new Background(R.drawable.spaceback_asteroids,new Point(0,0),-1);
				break;
			case 2:
				background = new Background(R.drawable.spaceback_nebula,new Point(0,0),-1);
				break;
		}
		//create a border for the background (draw on top)
		new Border(background,1000);
		//create the player in the middle of the background
		player = new Spaceship(new Point(0,0));
		//create a target reticule for the player
		target = new Target(player);
		
		//debug
		//create explosion
		new Enemy(new Point(150,150));
		
		//load sounds
		GameState.State().LoadSound(R.raw.shoot, "shoot");
		GameState.State().LoadSound(R.raw.accelerate, "accelerate");
		GameState.State().LoadSound(R.raw.engine, "engine");
		GameState.State().LoadSound(R.raw.explosion, "explosion");
		
		hasStarted = true;
	}
	
	@Override
	public void update() {
		if (!hasStarted){
			OnStart();
		}
		
		/**
		//once the screen loads, create the player at the center of the screen
		if (player == null && GameState.State().ScreenSize != null){
			player = new Spaceship(new Point(GameState.State().ScreenSize.div(2)));
		}
		
		//once the player exists, create the target circle
		if (target == null && player != null){
			target = new Target(player);
		}
		**/
		
		//make sure the player and lasers all wrap around the screen
		ScreenWrap(player);
		for (Laser l : GameObject.allObjectsOfType(Laser.class)){
			ScreenWrap(l);
		}
		
		//center camera on player
		GameState.State().SetCameraPosition(player.pos.translate(GameState.State().ScreenSize.div(-2)));
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
