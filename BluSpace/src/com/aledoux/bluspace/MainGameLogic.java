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
	
	/**
	 * An initialization function that's called the first time this object is updated
	 * IMPORTANT: do NOT try to replicate this behavior with a constructor - it WON'T work
	 */
	public void OnStart(){
		//create the space background and put its priority behind everything else
		new Background(R.drawable.spaceback_sml,GameState.State().ScreenSize.div(2),-1);
		
		hasStarted = true;
	}
	
	@Override
	public void update() {
		if (!hasStarted){
			OnStart();
		}
		
		//once the screen loads, create the player at the center of the screen
		if (player == null && GameState.State().ScreenSize != null){
			player = new Spaceship(GameState.State().ScreenSize.div(2));
		}
		
		//once the player exists, create the target circle
		if (target == null && player != null){
			target = new Target(player);
		}
	}

}
