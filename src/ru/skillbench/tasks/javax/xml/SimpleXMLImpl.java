package ru.skillbench.tasks.javax.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class SimpleXMLImpl implements SimpleXML {
    @Override
    public String createXML(String tagName, String textNode) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        String xmlString = null;
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element element = document.createElement(tagName);
            element.appendChild(document.createTextNode(textNode));
            document.appendChild(element);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            xmlString = writer.toString();
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    @Override
    public String parseRootElement(InputStream xmlStream) throws SAXException {
        String[] root = {null};
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(xmlStream, new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if(root[0] == null) {
                        root[0] = qName;
                    }
                }
            });
        } catch (ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return root[0];
    }
}