package com.extraction.pdf.parse;

import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Obj {
	
	private IEventData data;
	private Point coordinate;
	private Size size;
}
