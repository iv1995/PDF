package com.extraction.pdf.components.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import edu.stanford.nlp.ling.TaggedWord;

public class Utilities {
	
	/**
	 * Use it when you want to process a different file
	 * The new file should be located in the same folder as the new file
	 * 
	 * @param old the path to the old file
	 * @param name the name of the
	 * @return
	 */
	public static String changeSrcPdf(String old, String name){

		if (StringUtils.isEmpty(old) || StringUtils.isEmpty(name)) {
			return new String();
		} else if (!old.contains("/")) {
			return name;
		}

		/* The name of the PDF file follows the last / character */
		int index = old.lastIndexOf("/");
		String folder = old.substring(0, index + 1);

		return folder + name;
	}
	
	/**
	 * 
	 * It avoids whitespace characters and \n characters
	 * 
	 * @param box The string that may contain the code string
	 * @param obj The string we are looking for inside the box
	 * @return
	 */
	public static int[] indexesOf(String box, String obj) {
		
		if(StringUtils.isEmpty(box) || StringUtils.isEmpty(obj)) {
			return new int[]{-1,-1};
		}
		
		int[] location = {-1, -1};
		
		for(int i = 0; i < box.length(); i++) {
			
			if(Character.toLowerCase(box.charAt(i)) 
					!= 
			   Character.toLowerCase(obj.charAt(0))) {
				continue;
			}
			
			location[0] = i;
			int count = 0;
			
			for(int j = 0; j < obj.length(); j++) {
				
				while(String.valueOf( box.charAt(i) ).isBlank() && i < box.length() - 1) {	
					i++;
					count++;
					
					if(i == box.length()) {
						break;
					}
				}
				
				while(String.valueOf( obj.charAt(j) ).isBlank()) {
					j++;
					
					if(j == obj.length()) {
						if(i < box.length()) {
							location[1] = i;
						} else {
							location[1] = box.length() - 1;
						}
						break;
					}
				}
				
				if(i == box.length() || j == obj.length()) {
					break;
				}
				
				char b = Character.toLowerCase(box.charAt(i));
				char t = Character.toLowerCase(obj.charAt(j));
				
				if(b != t) {
					count = 0;
					i++;
					break;
				}
				
				if(j == obj.length() - 1) {
					location[1] = location[0] + count;
					i = box.length();
					break;
				}
				
				i++;
				count++;
			}
		}
		
		if(location[0] > location[1]) {
			location[0] = -1;
			location[1] = -1;
		}
		
		return location;
	}
	
	public static boolean containsNegative(int[] arr) {
		
		if(arr == null) {
			return false;
		}
		
		boolean positive = false;
		
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] < 0) {
				positive = true;
				break;
			}
		}
		
		return positive;
	}
	
	public static int[] slide(int[] arr, int quantity) {
		
		if(arr == null) {
			return new int[0];
		}
		
		int[] output = new int[arr.length];
		
		for(int i = 0; i < output.length; i++) {
			output[i] = arr[i] + quantity;
		}
		
		return output;
	}
	
	public static int relativeDistance(int[] index) {
		if(index == null) {
			return 0;
		}		
		
		return index[1];
	}
	
	public static boolean isTitleWord(TaggedWord element) {
		
		if(element == null) {
			return false;
		} else if(element.toString().isBlank()) {
			return false;
		} else if(!element.toString().contains("/")) {
			return false;
		} else if(element.toString().split("/").length != 2) {
			return false;
		}
		
		String word = element.toString().split("/")[0];
		String tag  = element.toString().split("/")[1];
		
		ArrayList<String> capitalized = new ArrayList<>();
		
		/*Noun*/
		capitalized.add("NN");
		capitalized.add("NNS");
		capitalized.add("NNP");
		capitalized.add("NNPS");
		
		/*Verb*/
		capitalized.add("VB");
		capitalized.add("VBD");
		capitalized.add("VBG");
		capitalized.add("VBN");
		capitalized.add("VBP");
		capitalized.add("VBZ");
		
		/*Adjective*/
		capitalized.add("JJ");
		capitalized.add("JJR");
		capitalized.add("JJS");
		
		/*Adverbs*/
		capitalized.add("RB");
		capitalized.add("RBR");
		capitalized.add("RBS");
		
		/*Numbers*/
		capitalized.add("CD");
		
		
		if(capitalized.contains(tag.toUpperCase())) {
			if(!isCapitalized(word)) {
				return false;
			}
		}
		
		return true;
	}

	/*Checks if the first character in a string is uppercase*/
	public static boolean isCapitalized(String word) {
		
		if(word == null) {
			return false;
		} else if(word.isBlank()) {
			return true;
		}
		
		return word.charAt(0) == Character.toUpperCase(word.charAt(0));
	}
	
	public static int length(int[] index) {
		if(index == null){
			return 0;
		}
		
		return index[1] - index[0];
	}
	
	public static String unused(String str, int[] begin) {
		
		if(begin == null || StringUtils.isEmpty(str)) {
			new String();
		} else if(begin.length != 2 || containsNegative(begin)) {
			new String();
		}
		
		int start = begin[1];
		
		return str.substring(start + 1, str.length());
	}
}
