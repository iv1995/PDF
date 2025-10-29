package com.extraction.pdf.parse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Point {
	
	private float x;
	private float y;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
