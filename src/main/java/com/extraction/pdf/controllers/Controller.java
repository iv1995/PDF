package com.extraction.pdf.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.extraction.pdf.parse.Book;

@RestController
public class Controller {
	
	@Autowired
	private Book book;
	
	/**
	 * 
	 * @param src The name of the PDF file that needs to processed
	 * 		      You have to move the file or a copy of it inside the 
	 * 			  ./Storage/Input/PDF/ 
	 * 		      prior to the call
	 * 	          otherwise the application won't find it
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@GetMapping(path = "/process/{src}")
	public void process(@PathVariable String src) throws URISyntaxException, IOException {
		book.processPdf(src);
	}
}
