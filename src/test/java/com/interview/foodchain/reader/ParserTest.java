package com.interview.foodchain.reader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ParserTest {

	private static final Logger LOG = Logger.getLogger(ParserTest.class);
	private static final String PROPERTY_FILE = "/foodchainTest.properties";
	private static final String INPUT_FILE_PATH = "input.filepath";
	
	@Test
	public void test_generated_files(){
		assert(true);
		try{
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream(PROPERTY_FILE));
			String filePath = properties.getProperty(INPUT_FILE_PATH);
			File directory = new File(filePath);
			File[] listOfFiles = directory.listFiles();
			List<FileParser> fileParsers = ParserFactory.getParser(listOfFiles);
			System.out.println("fileParser " + fileParsers);
			for(FileParser fileParser: fileParsers){
				Parser parser = new Parser(fileParser);
				parser.parseInputFile(fileParser, filePath);
			}
		}catch(Exception exception){
			LOG.error("Error occured while reading property file " + exception);
		}
	}

	@Test
	public void validate_xsd(){
		assert(true);
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream(PROPERTY_FILE));
			String xsdFilePath = properties.getProperty("input.xsdpath");
			String xmlFilePath = properties.getProperty("input.xmlpath");
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(xsdFilePath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(xmlFilePath)));
		} catch(SAXException e1){
			LOG.error("SAX Exception: "+e1.getMessage());
		}catch(IOException ioe){
			LOG.error("IO Exception " + ioe.getMessage());
		}
	}
}
