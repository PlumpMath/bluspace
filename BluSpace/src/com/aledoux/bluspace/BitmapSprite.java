package com.aledoux.bluspace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Sprite that uses bitmap images stored in resources
 * 
 * @author adamrossledoux
 *
 */
public class BitmapSprite extends Sprite {
	private Bitmap sourceBitmap, bitmap;
	private float rotationAngle;
	
	public BitmapSprite(int id){
		sourceBitmap = BitmapFactory.decodeResource(GameState.State().context.getResources(), id);
		bitmap = Bitmap.createBitmap(sourceBitmap);
 	}
	
	public BitmapSprite(Bitmap bmp){
		sourceBitmap = bmp;
		bitmap = Bitmap.createBitmap(sourceBitmap);
 	}
	
	@Override
	public void draw(Canvas canvas, Point pos) {
		canvas.drawBitmap(bitmap, pos.x - bitmap.getWidth()/2, pos.y - bitmap.getHeight()/2, new Paint());
	}
	
	public float setRotation(float degrees){
		rotationAngle = degrees;
		
		Matrix mtx = new Matrix();
		mtx.setRotate(rotationAngle);
		
		bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), mtx, true);
		
		return rotationAngle;
	}
	
	public float rotate(float degrees){
		rotationAngle += degrees;
		
		Matrix mtx = new Matrix();
		mtx.setRotate(rotationAngle);
		
		bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), mtx, true);
		
		return rotationAngle;
	}
	
	public float rotateTowards(Vector from, Vector to, float degrees, float range){
		if (from.cross(to) > range){ //to the left - turn left
			rotate(degrees);
		}
		else if (from.cross(to) < -range){ //to the right - turn right
			rotate(-degrees);
		}
		else if (from.dot(to) < 0){ //behind - turn left
			rotate(degrees);
		}
		//in front - do nothing
		
		return rotationAngle;
	}
	
	public int getWidth(){
		return bitmap.getWidth();
	}
	
	public int getHeight(){
		return bitmap.getHeight();
	}

}
