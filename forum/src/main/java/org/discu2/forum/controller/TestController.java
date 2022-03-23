package org.discu2.forum.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/test")
public class TestController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    private void testAuth(HttpServletRequest request, HttpServletResponse response) {

    }

}
