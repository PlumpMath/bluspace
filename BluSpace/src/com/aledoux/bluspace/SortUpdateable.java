package com.aledoux.bluspace;

import java.util.Comparator;

public class SortUpdateable implements Comparator<Updateable> {

	public int compare(Updateable u1, Updateable u2) {
		return u1.updatePriority() - u2.updatePriority();
	}

}
