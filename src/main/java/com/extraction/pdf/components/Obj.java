package com.extraction.pdf.components;

import java.util.Comparator;

import com.extraction.pdf.parse.Point;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Obj{
	
	private IEventData data;
	private Point coordinate;
	private Size size;
	
	public float getAbsoluteHeight() {
		return coordinate.getY() + size.getHeight();
	}
}
