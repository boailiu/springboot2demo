package com.boai.springboot2demo.Map;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class AbstractMapTest{

    public static void main(String[] args) {
        Map<Object, Object> hashMap = new LinkedHashMap<>();
        hashMap.put(1,1);
        hashMap.put(3,3);
        hashMap.put(2,2);
        hashMap.put(4,4);
        System.out.println(hashMap);
        System.out.println("====================");
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("1",1);
        treeMap.put("3",3);
        treeMap.put("2",2);
        treeMap.put("4",4);
        System.out.println(treeMap.firstKey());
        System.out.println(treeMap.lastEntry());
        System.out.println(treeMap.lastKey());
        System.out.println(treeMap);
        System.out.println(treeMap.floorEntry("0"));
        System.out.println(treeMap.ceilingEntry("5"));
        System.out.println(treeMap.headMap("2"));
        SortedMap<String, Object> sortedMap = treeMap.headMap("5");
        System.out.println(sortedMap.subMap("1", "4"));
        System.out.println("=======================");
        LinkedHashMap<Object, Object> linkedMap = new LinkedHashMap<>();
        linkedMap.put(1,1);
        linkedMap.put(3,3);
        linkedMap.put(2,2);
        linkedMap.put(4,4);
        System.out.println(linkedMap);

    }
}
