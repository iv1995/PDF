package com.extraction.pdf.parse;

import java.util.Comparator;

public class SortY implements Comparator<Obj>{

	@Override
	public int compare(Obj left, Obj right) {
		if(left.getAbsoluteHeight() > right.getAbsoluteHeight()) {
			return 1;
		} else if(left.getAbsoluteHeight() < right.getAbsoluteHeight()) {
			return -1;
		}
		
		return 0;
	}
}
