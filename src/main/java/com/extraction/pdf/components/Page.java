package com.extraction.pdf.components;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.springframework.util.MimeTypeUtils;
import com.extraction.pdf.components.utilities.Strings;
import com.extraction.pdf.ai.Chat;
import com.extraction.pdf.components.utilities.Array;
import com.extraction.pdf.sort.Sort;
import com.itextpdf.kernel.pdf.PdfName;

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
	 * @return The caption if a match has been found an empty string otherwise
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public String getCaption(Img img, boolean tagged) throws URISyntaxException, IOException {
		String caption = new String();
		
		StringBuilder above = piledupRows(img, true);
		StringBuilder below = piledupRows(img, false);
		
		PdfName tag = PdfName.H1;
		
		if(tagged) {
			caption = getCaption(tag, img).toString();
			
			int[] indexes = Strings.indexesOf(below.toString(), caption);
			
			if(Array.containsNegative(indexes)) {
				indexes = Strings.indexesOf(above.toString(), caption);
				
				if(Array.containsNegative(indexes)) {
					return new String();
				}
			}
		} else {
			System.out.println(Chat.processImageRequest("Which one of these do you think is the image caption? Return the one you think is correct only" + above.toString() + " OR " + below.toString(), new ByteArrayInputStream(img.masked()), MimeTypeUtils.IMAGE_JPEG));
		}
		
		return caption;
	}
	
	/**
	 * 
	 * @param img The image whose surrounding text we want to find
	 * @param above If true returns the text immediately above otherwise the one below
	 * @return The rows that are piled up one next to the other and immediately above the image
	 */
	private StringBuilder piledupRows(Img img, boolean above){
		
		float start = 0;
		
		/*
	 	* The objects have been ordered from top to bottom
	 	* By reading them from below it is easier to know which row is immediately above
	 	* */
		if(above) {
			Collections.reverse(objects);
			start = img.getAbsoluteHeight();
		} else {
			start = img.getCoordinate().getY();
		}
		
		
		StringBuilder piledup = new StringBuilder();
		
		boolean activated = false;
		float acceptable = 0;
		
		for (int i = 0; i < objects.size(); i++) {
			
			if(objects.get(i) instanceof Text) {
				Text now = (Text)objects.get(i);
				
				//We are not interested in the rows above/below the image
				if(now.getCoordinate().getY() < start == above) {
					continue;
				} else if(i < objects.size() - 1) {
					if(objects.get(i + 1) instanceof Text) {
						Text next = (Text)objects.get(i + 1);
						
						if(next.getCoordinate().getY() > start == above && !activated){
							/* Once we find another row we can set a limit
							 * to the distance that can exist between two consecutive the rows
							 */
							acceptable = Math.abs(next.getCoordinate().getY() - start);
							activated = true;
						}
						
						if(above) {
							piledup.append(new StringBuilder(now.getChunk()).reverse());
						} else {
							piledup.append(new StringBuilder(now.getChunk()));
						}
						
						if(!(Math.abs(next.getCoordinate().getY() - now.getCoordinate().getY()) <= acceptable) && activated) {
							break;
						}
					}
				}
			}
		}
		
		if(above) {
			piledup.reverse();
			Collections.reverse(objects);
		}
		
		return piledup;
	}
	
	/**
	 * 
	 * It works in a Tagged PDF
	 * 
	 * @param tag It is the name of the tag which surrounds the text
	 * @return The caption if found and an empty string otherwise
	 */
	private StringBuilder getCaption(PdfName tag, Img img){
		StringBuilder caption = new StringBuilder();
		
		for(int i = 0;  i < objects.size(); i++) {
			
			if(objects.get(i) instanceof Img && i < objects.size() -1) {
				
				//There might be more than one image
				if(!((Img)objects.get(i)).equals(img)) {
					continue;
				}
				
				if(objects.get(i + 1) instanceof Text) {
					Text enclosed = (Text)objects.get(i + 1);
					
					int cont = 0;
					
					while(enclosed.getRi().getCanvasTagHierarchy().get(0).getRole().equals(PdfName.H1)
							&& (i + 1 + cont < objects.size() - 1)) {
						
						if(objects.get(i + 1 + cont) instanceof Text) {
							enclosed = (Text)objects.get(i + 1 + cont);
						}
						
						caption.append(enclosed.getChunk());
						cont++;
					}
				}
			}
		}
		
		return caption;
	}
	
//	/**
//	 * 
//	 * For each title adds the # markdown symbol
//	 * 
//	 * @param all All the text contained in the PDF file
//	 * @param titles The titles contained in the PDF file 
//	 * @throws IOException 
//	 */
//	public void markdown(ArrayList<String> phrases, String symbol) throws IOException {
//		
//		if(StringUtils.isEmpty(before) || StringUtils.isEmpty(after)) {
//			getText();
//		}
//		
//		int[] indexes = {0, 0};
//			
//		for(String phrase: phrases) {
//			indexes = Strings.indexesOf(before, phrase);
//			
//			if(!Array.containsNegative(indexes)) {
//				String before = this.before.substring(0, indexes[0]);
//				String middle = symbol + this.before.substring(indexes[0], indexes[1] + 1);
//				String after = this.before.substring(indexes[1] + 1, this.before.length());
//				
//				this.before = before + middle + after;
//			}
//			
//			indexes = Strings.indexesOf(after, phrase);
//			
//			if(!Array.containsNegative(indexes)) {
//				String before = this.after.substring(0, indexes[0]);
//				String middle = symbol + this.after.substring(indexes[0], indexes[1] + 1);
//				String after = this.after.substring(indexes[1] + 1, this.after.length());
//				
//				this.after = before + middle + after;
//			}
//		}
//	}
}
