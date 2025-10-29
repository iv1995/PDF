package com.extraction.pdf.sort;

import java.util.Comparator;

import com.extraction.pdf.components.Obj;
import com.google.common.collect.ComparisonChain;

public class Sort implements Comparator<Obj>{

	@Override
	public int compare(Obj o1, Obj o2) {
		
		return ComparisonChain.start()
				.compare(o2.getCoordinate().getY(), o1.getCoordinate().getY())
				.compare(o1.getCoordinate().getX(), o2.getCoordinate().getX()).result();
	}

}
