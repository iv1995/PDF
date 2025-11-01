package com.extraction.pdf.components.utilities;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import edu.stanford.nlp.ling.TaggedWord;

public class Strings {
	
	/*Removes specified characters (char[] chars)*/
	public static ArrayList<TaggedWord> remove(String string, char[] chars){
		
		if(StringUtils.isEmpty(string)) {
			return new ArrayList<TaggedWord>();
		}
		
		ArrayList<TaggedWord> words = new ArrayList<>();
			
		for (String word : string.split(" ")) {
			for(char c : chars) {
				/*Skip empty characters*/
				if(c != Character.MIN_VALUE) {
					word = word.replaceAll(String.valueOf(c), new String());
				}
			}
			
			words.add(new TaggedWord(word));
		}
		
		return words;
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
	
	public static String unused(String str, int[] begin) {
		
		if(begin == null || StringUtils.isEmpty(str)) {
			new String();
		} else if(begin.length != 2 || Array.containsNegative(begin)) {
			new String();
		}
		
		int start = begin[1];
		
		return str.substring(start + 1, str.length());
	}
	
	public static int[] lastIndex(String str) {
		if(StringUtils.isEmpty(str)) {
			return new int[0];
		}
		
		int[] last = {str.length(), str.length()};
		
		return last;
	}
	
	public static boolean isPreceding(int[] left, int[] right) {
		
		if(left == null || right == null) {
			return false;
		} else if(left.length == 0 || right.length == 0) {
			return false;
		} else if(left.length != right.length) {
			return false;
		}
		
		return left[1] > right[0];
	}
	
	public static String getBetween(String str, int[] first, int[] next) {
		if(first == null || next == null || StringUtils.isEmpty(str)) {
			return "";
		} else if(Array.containsNegative(first) || Array.containsNegative(next)) {
			return "";
		} else if(isPreceding(first, next)) {
			return "";
		} else if(isBeyond(first, str.length()) || isBeyond(next, str.length())) {
			return "";
		}
		
		return str.substring(first[0], next[0]);
	}
	
	public static boolean isBeyond(int[] arr, int limit) {
		
		if(arr == null) {
			return false;
		}
		
		boolean positive = false;
		
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] > limit) {
				positive = true;
				break;
			}
		}
		
		return positive;
	}
}
