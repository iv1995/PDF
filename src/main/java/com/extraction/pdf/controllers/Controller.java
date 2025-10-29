package com.extraction.pdf.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.extraction.pdf.ai.Chat;
import com.extraction.pdf.parse.Book;



@RestController
public class Controller {
	
	@Autowired
	private Book book;
	
	@GetMapping(path = "/processPdf/{pdfSrc}")
	public void extractFromUntaggedPdf(@PathVariable String pdfSrc) throws URISyntaxException, IOException {
		book.processPdf();
	}
}
