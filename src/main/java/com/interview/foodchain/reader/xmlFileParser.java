package com.interview.foodchain.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.interview.foodchain.schema.generated.CmfoodchainType;
import com.interview.foodchain.schema.generated.OrderdetailType;

public class xmlFileParser implements FileParser {

	private static final Logger LOG = Logger.getLogger(xmlFileParser.class);
	private static final String PROPERTY_FILE = "/foodchainTest.properties";
	private static final String INPUT_XSD_PATH ="input.xsdpath";
	private static final String INPUT_XML_PATH ="input.xmlpath";

	public void parseFile(String file) throws Exception {
		if(validateXMLSchema()){
			readXML(file);
		}else{
			throw new Exception(" Input xml is not correct format ");
		}
	}

	private void readXML(String file) throws IOException{
		File[] filepath = new File(file).listFiles(new FilenameFilter(){
			public boolean accept(File directory, String fileName){
				return fileName.endsWith(".xml");
			}});

		for(File fileName:filepath){
			File xmlFile = new File(fileName.getCanonicalPath());
			JAXBContext jc;
			try {
				jc = JAXBContext.newInstance("com.interview.foodchain.schema.generated");
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				JAXBElement<?> element =(JAXBElement<?>)unmarshaller.unmarshal(new FileInputStream(xmlFile));
				CmfoodchainType cmFoodChain = (CmfoodchainType)element.getValue();

				if(!calculateOrdersTotal(cmFoodChain).equals(cmFoodChain.getBranch().getTotalcollection())){
					FileWriter mismatchFile = new FileWriter("C:\\Users\\HP\\Desktop\\files\\mismatch\\MismatchedXML.txt");
					PrintWriter printWriter = new PrintWriter(mismatchFile);
					printWriter.println("*****************************************");
					printWriter.println("Mismatched Data for " + cmFoodChain.getBranch().getLocation() + " location" + " with location Id "+cmFoodChain.getBranch().getLocationid() );
					printWriter.printf("Total Collection is %s ", cmFoodChain.getBranch().getTotalcollection());
					printWriter.println();
					printWriter.printf("Summation of Orders is %s ", calculateOrdersTotal(cmFoodChain));
					printWriter.println();
					printWriter.printf("Difference: Total Collection minus Orders Total is %s", cmFoodChain.getBranch().getTotalcollection() - calculateOrdersTotal(cmFoodChain));
					printWriter.println();
					printWriter.close();
				}
				else{
					FileWriter matchedFile = new FileWriter("C:\\Users\\HP\\Desktop\\files\\match\\MatchedXML.txt");
					PrintWriter printWriter = new PrintWriter(matchedFile);
					printWriter.print("");
					printWriter.close();
				}
			} catch (JAXBException  jaxe ) {
				LOG.error("Error occured while reading xml" + jaxe);
			}
		}
	}

	private Float calculateOrdersTotal(CmfoodchainType cmFoodChain) {
		Float ordersTotal = 0f;
		List<OrderdetailType> orders = cmFoodChain.getOrders().getOrderdetail();
		for(OrderdetailType order: orders){
			ordersTotal = ordersTotal + order.getBillamount();
		}
		return ordersTotal;
	}

	private boolean validateXMLSchema(){
		LOG.debug(" Inside validating XML schema ");
		boolean validXmlSchema = true;
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream(PROPERTY_FILE));
			String xsdFilePath = properties.getProperty(INPUT_XSD_PATH);
			String xmlFilePath = properties.getProperty(INPUT_XML_PATH);

			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(xsdFilePath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(xmlFilePath)));
		} catch(SAXException saxe){
			LOG.error("SAX Exception: "+ saxe.getMessage());
			validXmlSchema = false;
		}catch(IOException ioe){
			LOG.error("IO Exception: " + ioe.getMessage());
			validXmlSchema= false;
		}
		return validXmlSchema;
	}
}
