package com.extraction.pdf.components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

import com.extraction.pdf.sort.Sort;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.canvas.CanvasTag;

import lombok.Data;

@Data
public class Page {
	
	private ArrayList<Obj> objects = new ArrayList<>();
	private ArrayList<String> h1 = new ArrayList<>();
	
	public Page(ArrayList<Obj> objects) {
		populate(objects);
	}
	
	private boolean populate(ArrayList<Obj> objects) {
		if(objects != null) {
			this.objects.addAll(sort(objects));
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
	
	/**
	 * 
	 * @param img The image of which you want to find the caption
	 * @param tagged Informs the method if it is dealing with a tagged PDF
	 * @return
	 * @throws IOException 
	 */
	public StringBuilder getCaption(Img img, boolean tagged) throws IOException {
		ArrayList<StringBuilder> caption = new ArrayList<>();
		float[] start = borderingRows(new ArrayList<>(getShelves()), img);
		
		StringBuilder above = aboveRows(img, start[1]);
		StringBuilder below = belowRows(img, start[0]);
		
		PdfName tag = PdfName.H1;
		
		if(h1.size() == 0) {
			caption.addAll(getContent(tag));
		}
		
		System.out.println(above + "\n" + below + "\n");
		
		for(StringBuilder h : caption) {
			if(above.indexOf(h.toString()) >= 0 || below.indexOf(h.toString()) >= 0) {
				return h;
			}
		}
		
		return new StringBuilder();
	}
	
	/**
	 * 
	 * @param tag It is the name of the tag which surrounds the text
	 * @return
	 */
	private ArrayList<StringBuilder> getContent(PdfName tag){
		ArrayList<StringBuilder> text = new ArrayList<>();
		
		boolean activated = false;
		float y = 0;
		
		StringBuilder h1 = new StringBuilder();
		
		for(int i = 0;  i < objects.size(); i++) {
			if(objects.get(i) instanceof Text) {
				Text txt = (Text)objects.get(i);
				if(txt.getRi().getCanvasTagHierarchy().size() > 0) {
					if(txt.getRi().getCanvasTagHierarchy().get(0).getRole().equals(PdfName.H1)){
						activated = true;
						h1.append(txt.getChunk());
					} else if(activated){
						text.add(h1);
						h1 = new StringBuilder();
						activated = false;
					}
				}
			}
		}
		
		return text;
	}
	
	private StringBuilder aboveRows(Img img, float start){
		Collections.reverse(objects);
		StringBuilder piledup = new StringBuilder();
		
		boolean activated = false;
		float acceptable = 0;
		
		for (int i = 0; i < objects.size(); i++) {
			
			if(objects.get(i) instanceof Text) {
				Text now = (Text)objects.get(i);
				
				if(now.getCoordinate().getY() < start) {
					continue;
				} else if(i < objects.size() - 1) {
					if(objects.get(i + 1) instanceof Text) {
						Text next = (Text)objects.get(i + 1);
						
						if(now.getCoordinate().getY() >= start && !activated){
							acceptable = next.getCoordinate().getY() - start;
							activated = true;
						}
						
						piledup.append(new StringBuilder(now.getChunk()).reverse());
						
						if(!(next.getCoordinate().getY() - now.getCoordinate().getY() <= acceptable)) {
							break;
						}
					}
				}
			}
		}
		
		piledup.reverse();
		
		Collections.reverse(objects);
		
		return piledup;
	}
	
	private StringBuilder belowRows(Img img, float start){
		StringBuilder piledup = new StringBuilder();
		
		boolean activated = false;
		float acceptable = 0;
		
		for (int i = 0; i < objects.size(); i++) {
			
			if(objects.get(i) instanceof Text) {
				Text now = (Text)objects.get(i);
				
				if(now.getCoordinate().getY() > start) {
					continue;
				} else if(i < objects.size() - 1) {
					if(objects.get(i + 1) instanceof Text) {
						Text next = (Text)objects.get(i + 1);
						
						if(next.getCoordinate().getY() <= start && !activated){
							acceptable = next.getCoordinate().getY() - start;
							activated = true;
						}
						
						if(!(next.getCoordinate().getY() - now.getCoordinate().getY()  <= acceptable)) {
							break;
						}
						
						piledup.append(now.getChunk());
					}
				}
			}
		}
		
		return piledup;
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
	private float[] borderingRows(ArrayList<Float> bases, Img img) {
		float[] start = {0, Float.MAX_VALUE};
		
		for (int i = 0; i < bases.size(); i++) {
			float y = bases.get(i);
			
			if (isBelow(y, img) && y > start[0]) {
					start[0] = y;
			} else if (isAbove(y, img) && y < start[1]) {
					start[1] = y;
					break;
			}
		}
		return start;
	}
	
	private boolean isAbove(float y, Img img){
		return img.getCoordinate().getY() < y;
	}
	
	private boolean isBelow(float y, Img img){
		return img.getCoordinate().getY() > y;
	}
	
	/**
	 * 
	 * @return It returns the y coordinates of all the lines
	 */
	private TreeSet<Float> getShelves() {
		TreeSet<Float> shelves = new TreeSet<>();
		
		for(int i = 0; i < objects.size(); i++) {
			if(objects.get(i) instanceof Text) {
				float y = ((Text)objects.get(i)).getCoordinate().getY();
				shelves.add(y);
			}
		}
		
		return shelves;
	}
}
