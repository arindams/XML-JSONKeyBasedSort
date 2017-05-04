package com.xmljsonsortutil;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Created by asaha on 5/3/2017.
 */
public class MutiLineJsonToSingle {



    HashMap<DocumentContext,String> dcToJson= new HashMap<DocumentContext,String>();

    /**
     * Will contain Duplicates
     * Converts a Multi-line Json List into a Sorted json List Based on a JsonPath.
     * Considering the json keys are unordered user has to pass an existing key which will be used to sort the data.
     * @param multiLinejsonList
     * @param sortKeyPath
     * @param singleLine
     * @return List of Sorted json,each json in a single line
     */
    public List<String> getSortedJsonList(List<String> multiLinejsonList, String[] sortKeyPath, boolean singleLine){
        List<Container> jsonObjList = getSortedJson(multiLinejsonList,sortKeyPath, singleLine);
        List<String> sortedJsonList =   jsonObjList.stream().map((cont) -> { System.out.println(cont.getJson_xml()); return cont.getJson_xml();}).collect(Collectors.toList());
        return sortedJsonList;
    }

    /**
     * Duplicates will be ignored.
     * Converts a Multi-line Json List into a Sorted json Set Based on a JsonPath.
     * Considering the json keys are unordered user has to pass an existing key which will be used to sort the data.
     * @param multiLinejsonList
     * @param sortKeyPath
     * @param singleLine
     * @return Set of Sorted json,each json in a single line
     */
    public Set<String> getSortedJsonSet(List<String> multiLinejsonList, String[] sortKeyPath,boolean singleLine){
        List<Container> jsonObjList = getSortedJson(multiLinejsonList,sortKeyPath, singleLine);
        Set<String> sortedJsonSet = jsonObjList.stream().map((cont) -> { System.out.println(cont.getJson_xml()); return cont.getJson_xml();}).collect(Collectors.toSet());
        return sortedJsonSet;
    }

    private List<Container> getSortedJson(List<String> multiLinejsonList, String[] sortKeyPath, boolean singleLine ){
        if(singleLine){
            StringBuffer sbuff = new StringBuffer();
            multiLinejsonList = multiLinejsonList.stream().map((json) -> {
                try {
                    sbuff.delete(0, sbuff.length());
                    StringReader sr = new StringReader(json);
                    BufferedReader br = new BufferedReader(sr);
                    String line = null;
                    while( ( line= br.readLine()) != null){
                        sbuff.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return sbuff.toString();
            }).collect(Collectors.toList());
        }
        List<Container> jsonObjList =
                multiLinejsonList.stream().map((json) -> {
                            DocumentContext dc = JsonPath.parse(json);
                            String data = null;

                            if(sortKeyPath.length > 1){
                                data = "Sortvalues:";
                                for(String sortKey : sortKeyPath){
                                    data += dc.read(sortKey).toString()+Container.SORT_KEY_SEPERATOR;
                                }
                            }else{
                                data = dc.read(sortKeyPath[0]).toString();
                            }

                            Container cont = new Container(data,json);
                            return cont;
                        }
                ).collect(Collectors.toList());
        XMLJsonComparator jcomparator = new XMLJsonComparator();
        Collections.sort(jsonObjList, jcomparator);
        return jsonObjList;
    }
}
