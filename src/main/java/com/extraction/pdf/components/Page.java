package com.extraction.pdf.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import com.extraction.pdf.sort.Sort;

import lombok.Data;

@Data
public class Page {
	
	private ArrayList<Obj> objects = new ArrayList<>();
	
	public Page(ArrayList<Obj> objects) {
		populate(objects);
	}
	
	private boolean populate(ArrayList<Obj> objects) {
		if(objects != null) {
			objects.addAll(sort(objects));
		}
		
		return true;
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
	private ArrayList<Obj> sort(ArrayList<Obj> objects) {
		Comparator<Obj> comparator = new Sort();
		Collections.sort(objects, comparator);
		
		return objects;
	}
	
	/**
	 * 
	 * @param img The image of which you want to find the caption
	 * @param tagged Informs the method if it is dealing with a tagged PDF
	 * @return
	 */
	public String getCaption(Img img, boolean tagged) {
		String caption = new String();
		
		return caption;
	}
	
	/**
	 * 
	 * @return All the text contained in this page
	 */
	public String getText() {
		String text = new String();
		
		for(int i = 0; i < objects.size(); i++) {
			if(objects.get(i) instanceof Text) {
				Text obj = (Text)objects.get(i);
				text += obj.getChunk();
			}
		}
		
		return text;
	}
	
	private ArrayList<String> getPiledup(Img img){
		ArrayList<String> closest = new ArrayList<String>();
		SortedMap<Float, Float> rows = getRows();
		ArrayList<Float> y = new ArrayList<Float>(rows.keySet());
		
		for (int i = 0; i < rows.size() - 1; i++) {
			Float first = rows.get(y.get(i));
			Float second = rows.get(y.get(i + 1));
		}
		
		return closest;
	}
	
	/**
	 * 
	 * @param bases The y coordinates of the rows on this page
	 * @param img The image of which we want to find the surrounding text
	 * @return The y coordinates of three rows:
	 * 			float[0] closest row from below 
	 * 			float[1] closest row from above
	 * 
	 */
	private float[] getBeginnings(ArrayList<Float> bases, Img img) {
		float[] start = {0, 0};
		
		for (int i = 0; i < bases.size(); i++) {
			if (isAbove(bases.get(i), img)) {
				start[0] = bases.get(i - 1);
				start[1] = bases.get(i);
			}
		}
		
		return start;
	}
	
	private boolean isAbove(float y, Img img){
		return img.getCoordinate().getY() < y;
	}
	
	/**
	 * 
	 * @return It returns the y coordinates of all the lines of text
	 */
	private SortedMap<Float, Float> getRows() {
		SortedMap<Float, Float> bases = new TreeMap<>();
		
		for(int i = 0; i < objects.size(); i++) {
			if(objects.get(i) instanceof Text) {
				float y = ((Text)objects.get(i)).getCoordinate().getY();
				float font = ((Text)objects.get(i)).getFont();
				
				if(!bases.containsKey(y)) {
					bases.put(y, font);
				} else if(bases.get(y) < font){
					bases.replace(y, font);
				}
			}
		}
		
		return bases;
	}
}
