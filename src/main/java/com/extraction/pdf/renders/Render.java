package com.extraction.pdf.renders;

import java.util.ArrayList;
import java.util.Set;

import com.extraction.pdf.components.Img;
import com.extraction.pdf.components.Obj;
import com.extraction.pdf.components.Text;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;

import lombok.Data;


@Data
public class Render implements IEventListener {

	private ArrayList<Obj> objects = new ArrayList<>();

	public Render() {}

	@Override
	public void eventOccurred(IEventData data, EventType type) throws IOException {
		if (type.equals(EventType.RENDER_TEXT)) {
			objects.add(new Text(data));
		} else if (type.equals(EventType.RENDER_IMAGE)) {
				objects.add(new Img(data));
		}
	}

	@Override
	public Set<EventType> getSupportedEvents() {
		return null;
	}
}
