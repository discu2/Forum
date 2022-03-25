package org.discu2.forum.core.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/test")
public class TestController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    private Object[] testAuth(HttpServletRequest request, HttpServletResponse response) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
    }

}
