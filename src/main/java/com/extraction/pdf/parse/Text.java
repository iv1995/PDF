package com.extraction.pdf.parse;

import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.CharacterRenderInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Text extends Obj{
	
	private TextRenderInfo ri;
	private CharacterRenderInfo cr;
	private String chunk;
	private float leading;
	private float font;
}
