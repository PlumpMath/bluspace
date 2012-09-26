package com.aledoux.bluspace;
import android.graphics.Canvas;



public class Background extends RenderObject {

	BitmapSprite sprite;
	
	public Background(int picId, Point pos, int renderPriority){
		this.sprite = new BitmapSprite(picId);
		this.pos = pos;
		this.renderPriority = renderPriority;
	}
	
	@Override
	public void render(Canvas canvas) {
		sprite.draw(canvas,pos);
	}
	
	public int GetHeight(){
		return sprite.getHeight();
	}
	
	public int GetWidth(){
		return sprite.getWidth();
	}

}
