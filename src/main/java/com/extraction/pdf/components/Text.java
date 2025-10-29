package com.extraction.pdf.components;

import com.extraction.pdf.components.Point;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.CharacterRenderInfo;

import lombok.Data;

@Data
public class Text extends Obj{
	
	private TextRenderInfo ri;
	private CharacterRenderInfo cr;
	private String chunk;
	private float leading;
	private float font;
	
	public Text(IEventData data) {
		super.setData(data);
		ri = (TextRenderInfo) data;
		cr = new CharacterRenderInfo(ri);
		super.setCoordinate(new Point(cr.getBoundingBox().getX(), cr.getBoundingBox().getY()));
		super.setSize(new Size(cr.getBoundingBox().getHeight(), cr.getBoundingBox().getWidth()));
	}
}
