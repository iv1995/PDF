package com.extraction.pdf.components.utilities;

public class Array {
	
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
	
	public static int length(int[] index) {
		if(index == null){
			return 0;
		}
		
		return index[1] - index[0];
	}
}
