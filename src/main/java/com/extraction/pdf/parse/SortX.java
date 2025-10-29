package com.extraction.pdf.parse;

import java.util.Comparator;

public class SortX implements Comparator<Obj>{

	@Override
	public int compare(Obj left, Obj right) {
		
		if(left.getCoordinate().getY() == right.getCoordinate().getY()) {
			if(left.getCoordinate().getX() > right.getCoordinate().getX()) {
				return 1;
			} else if(left.getCoordinate().getX() < right.getCoordinate().getX()){
				return -1;
			}
		}
		
		return 0;
	}

}
