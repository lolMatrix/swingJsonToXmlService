package ru.test.rest.utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XmlParser {

    /**
     * parse raw xml string from soap service and get raw string without soap headers
     *
     * @param raw raw string from soap service
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public String getXmlFromSoapMessage(String raw) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new InputSource(new StringReader(raw)));
        return doc.getElementsByTagName("ns2:response").item(0).getFirstChild().getNodeValue();
    }

    /**
     * parse raw xml string and convert into xml document
     *
     * @param doc raw xml string
     * @return xml document
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public Document getXmlFromString(String doc) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new InputSource(new StringReader(doc)));
        return document;
    }

}
