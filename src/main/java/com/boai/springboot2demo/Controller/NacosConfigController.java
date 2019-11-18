package com.boai.springboot2demo.Controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("config")
public class NacosConfigController {

    @NacosValue(value = "${useLocalCache:false}",autoRefreshed = true)
    private boolean useLocalCache;

    @NacosValue(value = "${testString:boailiu}",autoRefreshed = true)
    private String testString;

    @GetMapping("/get")
    public Map<String, Object> getConfig() {
        final Map<String, Object> map = new HashMap<>();
        map.put("useLocalCache", useLocalCache);
        map.put("testString",testString);
        return map;
    }

    @GetMapping("/discovery")
    @ResponseBody
    public List<Instance> get(@RequestParam String serviceName) {
/*        try {
            return namingService.getAllInstances(serviceName);
        } catch (NacosException e) {
            e.printStackTrace();
        }*/
        return null;
    }
}
