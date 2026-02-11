package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserClientService {

//    With RestTemplate
   /* private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public Long getUserIdFromToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                userServiceUrl + "/checkToken",
                HttpMethod.GET,
                request,
                Long.class
        );

        return response.getBody();
    }*/



//    With FeignClient
    @Autowired
    private  UserFeignClientService userFeignClientService;
    public Long getUserIdFromToken(String token) {
        return userFeignClientService.checkToken(token);
    }


}
