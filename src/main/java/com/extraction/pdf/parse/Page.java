package com.extraction.pdf.parse;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {
	
	private ArrayList<Obj> objects = new ArrayList<>();
	
	public boolean addObj(Obj object) {
		objects.add(object);
		return true;
	}
}
