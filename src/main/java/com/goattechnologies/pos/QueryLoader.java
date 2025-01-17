package com.goattechnologies.pos;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** This class allows for the loading of queries from an XML file.
 * @Author Dinesh Balakrishnan
 */
public class QueryLoader {
    private Document doc;

    /**
     * Constructor for QueryLoader
     * @param xmlFilePath The path to the XML file containing the queries
     * @throws Exception if the XML file cannot be parsed
     */
    public QueryLoader(String xmlFilePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(xmlFilePath);
    }

    /**
     * Gets the query with the given id
     * @param queryId The id of the query
     * @return The query string
     */
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
