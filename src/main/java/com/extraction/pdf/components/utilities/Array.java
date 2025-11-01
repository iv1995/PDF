package com.extraction.pdf.components.utilities;

public class Array {
	
	/**
	 * 
	 * Checks if an array of integers contains at least one negative value
	 * 
	 * @param arr The array that needs to analyzed
	 * @return true if {@code arr} contains at least one negative value
	 */
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
	
	/**
	 * 
	 * It adds {@code quantity} to each element of the array
	 * 
	 * @param arr The array whose elements are going to be incremented with {@code quantity}
	 * @param quantity The amount that is going to be added to each element contained in {@code arr}
	 * @return
	 */
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
	
	/**
	 * 
	 * @param index It contains the index where a string starts and the index where it ends
	 * @return The index where the string ends
	 */
	public static int absoluteDistance(int[] index) {
		if(index == null) {
			return 0;
		}		
		
		return index[1];
	}
	
	/**
	 * 
	 * @param index It contains the index where a string starts and the index where it ends
	 * @return The length of the string that these indexes enclose
	 */
	public static int length(int[] index) {
		if(index == null){
			return 0;
		}
		
		return index[1] - index[0];
	}
}
