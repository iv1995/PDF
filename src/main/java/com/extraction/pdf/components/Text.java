package com.extraction.pdf.components;

import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.CharacterRenderInfo;

import lombok.Data;

@Data
public class Text extends Obj{
	
	private TextRenderInfo ri;
	private CharacterRenderInfo cri;
	private String chunk;
	private float leading;
	private float font;
	
	public Text(IEventData data) {
		if(data != null) {
			populate(data);
		}
	}
	
	private boolean populate(IEventData data) {
		
		super.setData(data);
		ri = (TextRenderInfo) data;
		cri = new CharacterRenderInfo(ri);
		super.setCoordinate(new Point(cri.getBoundingBox().getX(), cri.getBoundingBox().getY()));
		super.setSize(new Size(cri.getBoundingBox().getHeight(), cri.getBoundingBox().getWidth()));
		leading = ri.getLeading();
		font = ri.getFontSize();
		
		return true;
	}
}
