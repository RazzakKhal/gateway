package com.bookNDrive.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient("user-service")
public interface UserServiceFeignClient {


    @GetMapping("/users/validate")
    public ResponseEntity<Map<String, String>> validateToken(@RequestHeader("Authorization") String token);
}
