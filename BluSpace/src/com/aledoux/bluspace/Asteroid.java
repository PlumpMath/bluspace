package com.aledoux.bluspace;

import android.graphics.Canvas;
import android.graphics.Color;

public class Asteroid extends GameObject {
	private CircleSprite sprite;
	public static enum Size {BIG, MED, SML};
	private static int[] Widths = {200,100,60};
	private Size size;
	private int width;
	private Vector velocity;
	
	public Asteroid(Size size, Point pos, Vector velocity){
		this.pos = pos;
		this.velocity = velocity;
		this.size = size;
		this.width = Widths[size.ordinal()];
		this.sprite = new CircleSprite(getRadius(), Color.argb(255,255,255,255));
	}
	
	@Override
	public void update() {
		pos = pos.translate(velocity);
		
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
			for (int i = 0; i < (1 + (int)(Math.random()*2)); i++){
				Vector t = new Vector((float)(getRadius() - Math.random()*width),(float)(getRadius() - Math.random()*width));
				new Asteroid(Size.MED,this.pos.translate(t),new Vector((float)(-1 + Math.random()*2), (float)(-1 + Math.random()*2)));
			}
			break;
		case MED:
			GameObject.destroy(this);
			for (int i = 0; i < (2 + (int)(Math.random()*3)); i++){
				Vector t = new Vector((float)(getRadius() - Math.random()*width),(float)(getRadius() - Math.random()*width));
				new Asteroid(Size.SML,this.pos.translate(t),new Vector((float)(-1 + Math.random()*2), (float)(-1 + Math.random()*2)));
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
