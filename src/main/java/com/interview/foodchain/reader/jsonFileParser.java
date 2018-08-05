package com.interview.foodchain.reader;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

public class jsonFileParser implements FileParser {

	private static final Logger LOG = Logger.getLogger(jsonFileParser.class);

	public void parseFile(String file) {
		System.out.println(" inside json file parser");
		try{
			readJSONFile(file);
		}catch(Exception exception){
			LOG.error("Error occured while reading json ", exception);
		}
	}

	private void readJSONFile(String file) throws IOException {
		File[] filepath = new File(file).listFiles(new FilenameFilter(){
			public boolean accept(File directory, String fileName){
				return fileName.endsWith(".json");
			}});

		for(File fileName:filepath){
			File jsonFile = new File(fileName.getCanonicalPath());
			JSONParser parser =  new JSONParser();
			try{
				Object object= parser.parse(new FileReader(jsonFile));
				org.json.simple.JSONObject jsonObject =  (org.json.simple.JSONObject)object;
				String cmfoodchain = jsonObject.get("cmfoodchain").toString();
			}catch(Exception e){
				LOG.error("error occured ", e);
			}
		}
	}
}
