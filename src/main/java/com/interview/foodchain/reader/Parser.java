package com.interview.foodchain.reader;

public class Parser {
	private FileParser fileParser;
	
	public Parser(FileParser fileParser){
		this.fileParser = fileParser;
	}
	public void parseInputFile(FileParser parser, String file) throws Exception{
		parser.parseFile(file);
	}

}
