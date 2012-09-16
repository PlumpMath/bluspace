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
	private Spaceship player;
	private Target target;
	
	@Override
	public void update() {
		//once the screen loads, create the player at the center of the screen
		if (player == null && GameState.State().ScreenSize != null){
			player = new Spaceship(GameState.State().ScreenSize.div(2));
		}
		
		//once the player touches the screen, create the target circle
		if (target == null && GameState.State().lastTouch != null){
			target = new Target();
		}
	}

}
