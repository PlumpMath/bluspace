package com.aledoux.bluspace;

import android.graphics.Canvas;

/**
 * A sprite is just a way to draw things
 * It has no built-in position that it remembers,
 * so it needs to belong to something like a renderable
 * 
 * The difference between a SPRITE and a RENDERABLE
 * is that a sprite is a *way* to draw something and
 * a renderable is a *thing* you draw
 * 
 * @author adamrossledoux
 *
 */
public abstract class Sprite {
	public abstract void draw(Canvas canvas, Point pos);
}
