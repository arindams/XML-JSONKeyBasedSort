package com.xmljsonsortutil;

import java.util.Comparator;

/**
 * Created by asaha on 5/3/2017.
 */
public class XMLJsonComparator implements Comparator<Container> {

    boolean isXml = true;
    String sortKeyPath = null;

    public void setSortKeyPath(String sortKeyPath) {
        this.sortKeyPath = sortKeyPath;
    }

    public void setIsXml(boolean isXml) {
        this.isXml = isXml;
    }

    @Override
    public int compare(Container o1, Container o2) {
        return o1.getSortData().compareTo(o2.getSortData());
    }
}
