package com.xmljsonsortutil;

/**
 * Created by asaha on 5/3/2017
 */
public class Container {

    public static String SORT_KEY_SEPERATOR = "==SORT_KEY_SEPERATOR==";

    public String getSortData() {
        return sortData;
    }

    public String getJson_xml() {
        return json_xml;
    }

    String sortData = null;
    String json_xml = null;

    public Container(String sortData, String data){
        this.sortData = sortData;
        this.json_xml = data;
    }

    public String getJson_xml_1() {
        return json_xml;
    }

}
