package com.extraction.pdf.components.utilities;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import edu.stanford.nlp.ling.TaggedWord;

public class Strings {
	
	/**
	 * 
	 * 
	 * 
	 * @param string The string which is going to be processed
	 * @param chars The characters that must be removed from {@code string}
	 * @return {@code ArrayList<TaggedWord>}
	 */
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
	 * It avoids whitespace characters and \n characters when comparing two strings
	 * 
	 * @param box The string that may contain the code string
	 * @param obj The string we are looking for inside the box
	 * @return an array containing the indexes where {@code obj} starts and where where {@code obj} ends within {@code box}
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
	
	/**
	 * 
	 * @param str The string that is going to be split
	 * @param middle The starting index and the ending index of the part
	 * 			     that needs to be removed from {@code str}
	 * @return 
	 */
	public static String unused(String str, int[] middle) {
		
		if(middle == null || StringUtils.isEmpty(str)) {
			new String();
		} else if(middle.length != 2 || Array.containsNegative(middle)) {
			new String();
		}
		
		return str.substring(0, middle[0]) 
			 + str.substring(middle[1] + 1, str.length());
	}
	
	/**
	 * 
	 * @param str The string that is going to be processed
	 * @return An array with both elements containing the last index of {@code str}
	 */
	public static int[] lastIndex(String str) {
		if(StringUtils.isEmpty(str)) {
			return new int[0];
		}
		
		int[] last = {str.length(), str.length()};
		
		return last;
	}
	
	/**
	 * 
	 * @param left The indexes of the first string
	 * @param right The indexes of the second string
	 * @return true if the first string comes before the other string
	 */
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
	
	/**
	 * 
	 * @param str  the string that contains both strings with indexes first and next
	 * @param first the string that appears first within str
	 * @param next the string that appears after first within str
	 * @return the part of str that is located between first and next
	 */
	public static String getBetween(String str, int[] first, int[] next) {
		if(first == null || next == null || StringUtils.isEmpty(str)) {
			return new String();
		} else if(Array.containsNegative(first) || Array.containsNegative(next)) {
			return new String();
		} else if(isPreceding(first, next)) {
			return new String();
		} else if(isBeyond(first, str.length()) || isBeyond(next, str.length())) {
			return new String();
		}
		
		return str.substring(first[0], next[0]);
	}
	
	/**
	 * 
	 * @param arr the array whose values are being examined
	 * @param limit the limit that each values within the array should not surpass
	 * @return true if the limit has been surpassed at least by one value within arr
	 */
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
