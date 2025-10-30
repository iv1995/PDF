package com.extraction.pdf.components;

import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import lombok.Data;


@Data
public class Obj{
	
	private IEventData data;
	private Point coordinate;
	private Size size;
	
	public float getAbsoluteHeight() {
		return coordinate.getY() + size.getHeight();
	}
}
