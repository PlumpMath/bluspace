package com.aledoux.bluspace;


/**
 * represents a mathematical vector (useful for physics)
 * @author Adam Le Doux
 *
 */
public class Vector {
	public float x,y;
	
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector add(Vector other){
		return new Vector(this.x + other.x, this.y + other.y);
	}
	
	public Vector sub(Vector other){
		return new Vector(this.x - other.x, this.y - other.y);
	}
	
	public Vector mult(float scalar){
		return new Vector(x*scalar,y*scalar);
	}
	
	public Vector div(float scalar){
		return new Vector(x/scalar,y/scalar);
	}
	
	public static float Distance(Vector a, Vector b){
		return (float) Math.sqrt(Math.pow((a.x - b.x),2) + Math.pow((a.y - b.y),2));
	}
}
