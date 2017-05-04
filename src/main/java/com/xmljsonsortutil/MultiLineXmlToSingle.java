package com.xmljsonsortutil;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by asaha on 5/3/2017.
 */
public class MultiLineXmlToSingle {



    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();
    DocumentBuilderFactory dbf = null;
    DocumentBuilder db = null;

    public MultiLineXmlToSingle(){
        dbf = DocumentBuilderFactory.newInstance();
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Will contain Duplicates
     * Gets a list of xml in string format and returns a sorted list based on the key provided in Xpath
     * @param xmlList
     * @param XpathSortKey
     * @param singleLine
     * @return List of Sorted xml, each xml in a single line
     */
    public List<String> getSortedXMLList(List<String> xmlList, String[] XpathSortKey, boolean singleLine){
        List<Container> xmlDocList = getSortedXML(xmlList,XpathSortKey,singleLine);
        List<String> sortedXmlList =  xmlDocList.stream().map( (doc) -> {
            System.out.println(doc.getJson_xml());
            return doc.getJson_xml();
        }).collect(Collectors.toList());
        return sortedXmlList;
    }

    /**
     * No Duplicates.
     * Gets a list of xml in string format and returns a sorted set based on the key provided in Xpath
     * @param xmlList
     * @param XpathSortKey
     * @param singleLine
     * @return Set of Sorted xml ,each xml in a single line
     */
    public Set<String> getSortedXMLSet(List<String> xmlList, String[] XpathSortKey, boolean singleLine){
        List<Container> xmlDocList = getSortedXML(xmlList,XpathSortKey,singleLine);
        Set<String> sortedXmlSet =  xmlDocList.stream().map( (doc) -> {
            System.out.println(doc.getJson_xml());
            return doc.getJson_xml();
        }).collect(Collectors.toSet());
        return sortedXmlSet;
    }

    public List<Container> getSortedXML(List<String> xmlList, String[] XpathSortKey, boolean singleLine){
        if(singleLine){
            StringBuffer sbuff = new StringBuffer();
            xmlList = xmlList.stream().map((xml) -> {
                try {
                    sbuff.delete(0, sbuff.length());
                    StringReader sr = new StringReader(xml);
                    BufferedReader br = new BufferedReader(sr);
                    String line = null;
                    while( ( line= br.readLine())!=null){
                        sbuff.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return sbuff.toString();
            }).collect(Collectors.toList());
        }

        List<Container> xmlDocList = xmlList.stream().map((xml) -> {
            Document doc = null;
            Container cont = null;
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(),0,xml.length());
                doc = db.parse(bais);
                String val = null;
                try {
                    if(XpathSortKey.length > 1){
                        val = "Sortvalues:";
                        for(String path : XpathSortKey){
                            val += xpath.evaluate(path, doc)+Container.SORT_KEY_SEPERATOR;
                        }
                    }else{
                        val = xpath.evaluate(XpathSortKey[0], doc);
                    }

                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
                cont=  new Container(val,xml);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cont;
        }).collect(Collectors.toList());

        XMLJsonComparator xmlComparator = new XMLJsonComparator();
        Collections.sort(xmlDocList,xmlComparator);
        return xmlDocList;
    }
}
