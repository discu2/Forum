package org.discu2.forum.core.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("forum-account")
public interface ServiceAccount {


}
