package com.extraction.pdf.parse;

import com.itextpdf.kernel.geom.Matrix;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Img extends Obj {
	
	private Matrix ctm;
}
