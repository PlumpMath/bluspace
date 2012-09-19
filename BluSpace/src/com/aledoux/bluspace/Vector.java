package com.aledoux.bluspace;


/**
 * represents a mathematical vector (useful for physics)
 * @author Adam Le Doux
 *
 */
public class Vector {
	public float x,y;
	
	/**
	 * create a vector with x and y displacement
	 * @param x
	 * @param y
	 */
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Create a vector using from the displacement of a starting point to an end point
	 * @param origin
	 * @param end
	 */
	public Vector(Point origin, Point end){
		this.x = end.x - origin.x;
		this.y = end.y - origin.y;
	}
	
	/**
	 * Create a vector from the displacement from the origin to an end point
	 * @param angle
	 */
	public Vector(Point end){
		this(Point.ORIGIN,end);
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
		return Point.Distance(Point.ORIGIN, new Point(this));
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
	
	//returns a vector perendictular (90 degrees from) the original
	public Vector perp(){
		return new Vector(-this.y, this.x);
	}
	
	//scalar cross product (like Jarek taught us ;) )
	//public float cross(Vector other){
		//return (this.x * other.y) + (-this.y * other.x);
	//}
	public float cross(Vector other){
		return (this.perp()).dot(other);
	}
	
	//dot product
	public float dot(Vector other){
		return (this.x * other.x) + (this.y * other.y);
	}
	
	//static methods
	public static float Dot(Vector a, Vector b){
		return a.dot(b);
	}
	
	public static float Cross(Vector a, Vector b){
		return a.cross(b);
	}
}
