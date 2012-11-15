package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;

public class Asteroid extends GameObject {
	//private CircleSprite sprite;
	private BitmapSprite sprite;
	public static enum Size {BIG, MED, SML};
	//private static int[] Widths = {200,100,60};
	private static int[] ImgIDs = {R.drawable.asteroid_lrg, R.drawable.asteroid_med, R.drawable.asteroid_sml};
	private Size size;
	private int width;
	//private Vector velocity;
	
	public Asteroid(Size size, Point pos, Vector velocity){
		this.pos = pos;
		this.velocity = velocity;
		this.size = size;
		this.sprite = new BitmapSprite(ImgIDs[size.ordinal()]);
		this.width = this.sprite.getWidth();
		//this.width = Widths[size.ordinal()];
		//this.sprite = new CircleSprite(getRadius(), Color.argb(255,255,255,255));
	}
	
	@Override
	public void update() {
		pos = pos.translate(velocity.mult(GameState.State().deltaTime()));
		
		for (Laser l : GameObject.allObjectsOfType(Laser.class)){
			if (collision(l.pos,0)){
				explode();
				GameObject.destroy(l);
			}
		}
	}
	
	public void explode(){
		switch(size){
		case BIG:
			GameObject.destroy(this);
			for (int i = 0; i < 2; i++){
				Vector t = new Vector((float)(Math.sin(i)*width),(float)(Math.cos(i)*width));
				new Asteroid(Size.MED,this.pos.translate(t.div(2)),t);
			}
			break;
		case MED:
			GameObject.destroy(this);
			for (int i = 0; i < 3; i++){
				Vector t = new Vector((float)(Math.sin(i)*width),(float)(Math.cos(i)*width));
				new Asteroid(Size.SML,this.pos.translate(t.div(2)),t);
			}
			break;
		case SML:
			GameObject.destroy(this);
			break;
		}
		GameState.State().PlaySound("explosion");
	}

	@Override
	public void render(Canvas canvas) {
		sprite.draw(canvas,pos);
	}
	
	public int getRadius(){
		return width/2;
	}
	
	/**
	 * does this asteroid collide with a circle?
	 * @param p Center of the colliding circle
	 * @param radius Radius of the colliding circle
	 * @return did it collide?
	 */
	public boolean collision(Point p, int radius){
		return (Point.Distance(pos, p) <= getRadius() + radius);
	}
}
