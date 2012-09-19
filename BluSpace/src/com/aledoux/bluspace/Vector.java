package com.aledoux.bluspace;


/**
 * represents a mathematical vector (useful for physics)
 * @author Adam Le Doux
 *
 */
public class Vector {
	public float x,y;
	
	/**
	 * create a vector from global origin
	 * @param x
	 * @param y
	 */
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Create a vector using one vector to point towards the origin
	 * and other to point towards its end point
	 * @param origin
	 * @param end
	 */
	public Vector(Vector origin, Vector end){
		this.x = end.x - origin.x;
		this.y = end.y - origin.y;
	}
	
	public Vector(float angle){
		float radians = (float) Math.toRadians(angle);
		this.x = android.util.FloatMath.cos(radians);
		this.y = android.util.FloatMath.sin(radians);
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
	
	//return the magnitude of the vector
	public float mag(){
		return Vector.Distance(new Vector(0,0), this);
	}
	
	//return the unit vector (magnitude = 1)
	public Vector unit(){
		if (mag() == 0){
			return this; //it's the zero vector
		}
		else{
			return this.div(mag());
		}
	}
	
	//scalar cross product (like Jarek taught us ;) )
	public float cross(Vector other){
		return (this.x * other.y) + (-this.y * other.x);
	}
	
	//dot product
	public float dot(Vector other){
		return (this.x * other.x) + (this.y * other.y);
	}
	
	public static float Distance(Vector a, Vector b){
		return android.util.FloatMath.sqrt((float) (Math.pow((a.x - b.x),2) + Math.pow((a.y - b.y),2)));
	}
}
