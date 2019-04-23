package com.boai.springboot2demo.Util;

import org.junit.Test;

import java.util.*;

public class ListTest {

    @Test
    public void listRemoveTest() {
        List<Map<String, Object>> objects = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("ruleName", "申请人风险");
        objects.add(map1);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("ruleName", "车辆风险");
        objects.add(map2);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("ruleName", "人加车风险");
        objects.add(map3);
        Map<String, Object> map4 = new HashMap<>();
        map4.put("ruleName", "人风险");
        objects.add(map4);
        Map<String, Object> map5 = new HashMap<>();
        map5.put("ruleName", "车风险");
        objects.add(map5);
        System.out.println(objects);

/*        objects.forEach(map -> {
            Object ruleName = map.get("ruleName");
            if (ruleName.equals("车风险")) {
                objects.remove(map);
            }
        });*/

        objects.removeIf(next -> next.get("ruleName").equals("车辆风险"));

        System.out.println(objects);

    }
}
