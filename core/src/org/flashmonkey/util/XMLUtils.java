package org.flashmonkey.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XMLUtils {
	
	public static Document createNewDocument(String rootNodeName) {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element root = doc.createElement(rootNodeName);
			doc.appendChild(root);
			
			return doc;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static Document parseFile(String fileName) {
		System.out.println("Parsing XML file... " + fileName);

		DocumentBuilder docBuilder;
		Document doc = null;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("Wrong parser configuration: " + e.getMessage());
			return null;
		}
		File sourceFile = new File(fileName);
		try {
			doc = docBuilder.parse(sourceFile);
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("Could not read source file: " + e.getMessage());
		}

		return doc;
	}

	public static boolean saveDocument(String fileName, Document doc) {
		System.out.println("Saving XML file... " + fileName);
		// open output stream where XML Document will be saved
		File xmlOutputFile = new File(fileName);
		FileOutputStream fos;
		Transformer transformer;
		try {
			fos = new FileOutputStream(xmlOutputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Error occured: " + e.getMessage());
			return false;
		}
		// Use a Transformer for output
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			System.out.println("Transformer configuration error: "
					+ e.getMessage());
			return false;
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(fos);
		// transform source into result will do save
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			System.out.println("Error transform: " + e.getMessage());
		}

		return true;
	}
	
	public static int countChildNodes(String fileName) {
		Document doc = parseFile(fileName);
		return doc.getFirstChild().getChildNodes().getLength();
	}
	
	public static String createPrintableString(Document doc) {
		String s = "";
		try {
			// set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
	
			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
	
			s = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return s;
	}
}
