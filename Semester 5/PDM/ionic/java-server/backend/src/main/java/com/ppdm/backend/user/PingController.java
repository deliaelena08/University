package com.ppdm.backend.user;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class PingController {
    @GetMapping("/api/ping")
    public String ping() {
        return "pong";
    }
}
