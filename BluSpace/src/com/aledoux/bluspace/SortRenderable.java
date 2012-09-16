package com.aledoux.bluspace;

import java.util.Comparator;

/**
 * A class the compares the priorities of two renderable objects
 * @author adamrossledoux
 *
 */
public class SortRenderable implements Comparator<Renderable>{

	public int compare(Renderable r1, Renderable r2) {
		return r1.renderPriority() - r2.renderPriority();
	}

}
