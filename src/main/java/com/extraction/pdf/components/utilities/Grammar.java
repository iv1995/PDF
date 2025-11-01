package com.extraction.pdf.components.utilities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

public class Grammar {
	
	public ArrayList<String> check(ArrayList<String> phrases, String model){
		
		if(phrases == null) {
			return new ArrayList<String>();
		} else if (StringUtils.isEmpty(model)) {
			model = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
		}
		
		LexicalizedParser parser = LexicalizedParser.loadModel(model);
		ArrayList<String> checked = new ArrayList<>();
		
		char[] unwanted = {'/', '\n', ',', ':'};
		
		for(int i = 0; i < phrases.size(); i++) {
			ArrayList<TaggedWord> words = Strings.remove(phrases.get(i), unwanted);
			Tree parsed = parser.apply(words);
			
			/*It tells me if it is a Noun, Verb, Adjective, ...*/
			ArrayList<TaggedWord> tagged = parsed.taggedYield();
			
			if(isCorrect(tagged)) {
				checked.add(phrases.get(i));
			}
		}
		
		return checked;
	}

	/*Checks if the title is grammatically correct*/
	public static boolean isCorrect(ArrayList<TaggedWord> title) {
		
		if(title == null) {
			return false;
		}
		
		for(TaggedWord word : title) {
			if(word == null) {
				continue;
			} else if(!isTitleWord(word)) {
				return false;
			}
		}
		
		return true;
	}
	
	/*Checks if the first character in a string is uppercase*/
	public static boolean isCapitalized(String word) {
		
		if(word == null) {
			return false;
		} else if(word.isBlank()) {
			return true;
		}
		
		return word.charAt(0) == Character.toUpperCase(word.charAt(0));
	}
	
	public static boolean isTitleWord(TaggedWord element) {
		
		if(element == null) {
			return false;
		} else if(StringUtils.isEmpty(element.toString())) {
			return false;
		} else if(element.toString().split("/").length != 2) {
			return false;
		}
		
		String word = element.toString().split("/")[0];
		String tag  = element.toString().split("/")[1];
		
		ArrayList<String> capitalized = new ArrayList<>();
		
		/*Noun*/
		capitalized.add("NN");
		capitalized.add("NNS");
		capitalized.add("NNP");
		capitalized.add("NNPS");
		
		/*Verb*/
		capitalized.add("VB");
		capitalized.add("VBD");
		capitalized.add("VBG");
		capitalized.add("VBN");
		capitalized.add("VBP");
		capitalized.add("VBZ");
		
		/*Adjective*/
		capitalized.add("JJ");
		capitalized.add("JJR");
		capitalized.add("JJS");
		
		/*Adverbs*/
		capitalized.add("RB");
		capitalized.add("RBR");
		capitalized.add("RBS");
		
		/*Numbers*/
		capitalized.add("CD");
		
		
		if(capitalized.contains(tag.toUpperCase())) {
			if(isCapitalized(word)) {
				return false;
			}
		}
		
		return true;
	}
	
	/*It is mainly used to find titles in an untagged PDF file*/
	public ArrayList<String> isAcceptable(String text) throws IOException{
		
		ArrayList<String> titles = new ArrayList<>();
		titles.add(new String());
		
		String current = new String();
		String next = new String();
		int count = 0;
		
		Scanner input = new Scanner(text);
		
		while(input.hasNext()) {
			if(current.isEmpty()) {
				do {
					if(input.hasNext()) {
						current = input.nextLine();
					} else {
						break;
					}
				}while(current.isEmpty());
				
				if(current.isEmpty()) {
					break;
				}
			}
			
			if(next.isEmpty()) {
				if(input.hasNext()) {
					next = input.nextLine();
				} else {
					break;
				}
			}
			
			if(current.length() >= next.length()) {
				if(current.trim().endsWith(".") || current.contains(". ") || current.isEmpty() || !isCapitalized(current)) {
					current = next;
					next = "";
					titles.add("");
					count++;
					continue;
				}
				
				titles.set(count, titles.get(count) + current + "\n");
				
				if(current.trim().endsWith(".") || next.contains(". ") || next.isEmpty() || !isCapitalized(next)) {
					current = "";
					next = "";
					titles.add("");
					count++;
					continue;
				}
				
				titles.set(count, titles.get(count) + next + "\n");
				
				current = next;
				next = "";
				titles.add("");
				count++;
			} else if(current.length() < next.length()) {
				
				boolean saved = false;
				
				if(titles.get(count - 1).contains(current)) {
					current = next;
					next = "";
					titles.add("");
					count++;
					continue;
				} else if(current.trim().endsWith(".") || current.contains(". ") || current.isEmpty() || !isCapitalized(current)) {
					current = next;
					next = "";
					titles.add("");
					count++;
					continue;
				}
				
				titles.set(count, current + "\n");
				current = next;
				next = "";
				titles.add("");
				count++;
				continue;
			}
		}
		
		input.close();
		
		return check(titles, "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		
	}
}
