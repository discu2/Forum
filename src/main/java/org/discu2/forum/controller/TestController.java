package org.discu2.forum.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/get")
    public Map<?,?> get(){
        var map = new HashMap<String, Object>();
        map.put("foo", "bar");
        return map;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void admin(){
        get();
    }

}
