package com.extraction.pdf.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lombok.Data;

@Data
public class Page {
	
	private ArrayList<Obj> objects = new ArrayList<>();
	
	public Page(ArrayList<Obj> objects) {
		this.objects.addAll(objects);
	}
	
	/**
	 * 
	 * @param object the object you want to add
	 * @return
	 */
	public boolean addObj(Obj object) {
		objects.add(object);
		return true;
	}
	
	/**
	 * @param number the position within the array of the object you want to remove
	 * @return
	 */
	public boolean removeObj(int number) {
		objects.remove(number);
		return true;
	}
	
	/**
	 * It sorts the objects based on their x and y coordinates
	 * @return
	 */
	public boolean sort() {
		Comparator<Obj> comparator = new Sort();
		Collections.sort(objects, comparator);
		
		return true;
	}
}
