package com.example.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name ="user-service" ,url = "${user.service.url}")
public interface UserFeignClientService {

    @GetMapping("/checkToken")
    Long checkToken(@RequestHeader("Authorization") String token);
}
