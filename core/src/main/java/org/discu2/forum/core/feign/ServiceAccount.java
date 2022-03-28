package org.discu2.forum.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FeignClient("forum-account")
public interface ServiceAccount {

    @RequestMapping("/account/login")
    HttpServletResponse login(HttpServletRequest request);
}
