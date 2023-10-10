package com.goattechnologies.pos;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class QueryLoader {
    private Document doc;

    public QueryLoader(String xmlFilePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(xmlFilePath);
    }

    public String getQuery(String queryId) {
        NodeList nodes = doc.getElementsByTagName("query");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            if (element.getAttribute("id").equals(queryId)) {
                return element.getTextContent().trim();
            }
        }
        return null;
    }
}
