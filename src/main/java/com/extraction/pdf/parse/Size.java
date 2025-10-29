package com.extraction.pdf.parse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Size {
	
	private float height;
	private float width;
	
	public Size(float height, float width) {
		this.height = height;
		this.width = width;
	}
}
