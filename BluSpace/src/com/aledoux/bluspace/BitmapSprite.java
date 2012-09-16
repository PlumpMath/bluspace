package com.aledoux.bluspace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Sprite that uses bitmap images stored in resources
 * 
 * @author adamrossledoux
 *
 */
public class BitmapSprite extends Sprite {
	private Bitmap bitmap;
	
	public BitmapSprite(int id){
		bitmap = BitmapFactory.decodeResource(GameState.State().context.getResources(), id);
	}
	
	@Override
	public void draw(Canvas canvas, Vector pos) {
		canvas.drawBitmap(bitmap, pos.x - bitmap.getWidth()/2, pos.y - bitmap.getHeight()/2, new Paint());
	}

}
