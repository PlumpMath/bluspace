package com.aledoux.bluspace;

/**
 * Represents a mathematical point (a position in 2D space)
 * @author adamrossledoux
 *
 */
public class Point {
	public static final Point ORIGIN = new Point(0,0); //the origin of the 2D plane
	
	public float x,y;
	
	/**
	 * create a point given an x and a y
	 * @param x
	 * @param y
	 */
	public Point(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * create a point given a start point 
	 * and a vector displacement to the new point
	 * @param p
	 * @param v
	 */
	public Point(Point p, Vector v){
		this.x = p.x + v.x;
		this.y = p.y + v.y;
	}
	
	/**
	 * create a point displaced from the origin
	 * by a vector v
	 * @param v
	 */
	public Point(Vector v){
		this(Point.ORIGIN,v);
	}
	
	/**
	 * displaces a point by a vector and returns the new position
	 * @param v
	 * @return the resulting point
	 */
	public Point move(Vector v){
		return new Point(this,v);
	}
	
	/**
	 * The distance between two points
	 * @param a
	 * @param b
	 * @return
	 */
	public static float Distance(Point a, Point b){
		return android.util.FloatMath.sqrt((float) (Math.pow((a.x - b.x),2) + Math.pow((a.y - b.y),2)));
	}
}
