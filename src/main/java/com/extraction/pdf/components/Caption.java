package com.extraction.pdf.components;

import lombok.Data;

@Data
public class Caption {
	
	private int number;
	
	private String caption;
	private String above;
	private String left;
	private String right;
	private String below;
	
	public Caption() {}
}
