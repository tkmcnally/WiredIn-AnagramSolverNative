package com.wiredin.anagramsolver.util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Thomas on 9/25/13.
 */
public class Util {

    public static String sanitize(String input) {
        String clean = input;
        clean = clean.replaceAll("[^a-zA-Z]+", "");
        return clean;
    }

    public static String parseXML(String xmlData) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        if("exception".equals(xmlData)) {
            return "Network connection failed!";
        }
        InputSource source = new InputSource(new StringReader(xmlData)); //or use your own input source

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        String definition = xpath.evaluate("/WordDefinition/Definitions/Definition/WordDefinition", document);
        //String status = xpath.evaluate("/resp/status", document);

     //   System.out.println("msg=" + msg + ";" + "status=" + status);

        if(definition == null || definition.isEmpty()) {
            definition = "Could not retrieve definition";
        }
        return definition;
    }

}
