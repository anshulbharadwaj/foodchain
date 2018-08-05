package com.interview.foodchain.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParserFactory {

	public static List<FileParser> getParser(File[] files){
		List<FileParser> fileParser = new ArrayList<FileParser>(); 
		for(File fileType: files){
			if(fileType.getName().endsWith(".xml")){
				System.out.println(" xml obj");
				fileParser.add(new xmlFileParser());
			}else if(fileType.getName().endsWith(".json")){
				System.out.println(" json obj");
				fileParser.add(new jsonFileParser());
			}
		}
		return fileParser;
	}
}
