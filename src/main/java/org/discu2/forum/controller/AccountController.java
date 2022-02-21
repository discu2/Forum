package org.discu2.forum.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/api/account")
public class AccountController {

    @PostMapping("/login")
    public String login(@RequestParam("user") String user, @RequestParam("pw") String pw, Map<String,Object> map) {
        map.put("test", "yes");
        return "/api/user/login";
    }

}
