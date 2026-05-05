package com.tcs.usermanagement.controller;

import com.tcs.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/demo")
@CrossOrigin(origins = "*")
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/sql/users")
    public List<Map<String, Object>> getUsersWithUnsafeSql(
            @RequestParam(defaultValue = "demo.user@example.com") String name) {
        return userService.searchUsersWithSqlInjection(name);
    }

    @GetMapping(value = "/xss", produces = MediaType.TEXT_HTML_VALUE)
    public String reflectedXss(@RequestParam(defaultValue = "<b>demo</b>") String input) {
        String escapedInput = HtmlUtils.htmlEscape(input);
        log.warn("Reflecting HTML-escaped input back to the caller");
        return "<html><body><h1>User Input</h1><div>" + escapedInput + "</div></body></html>";
    }

    @GetMapping("/slow")
    public Map<String, Object> slowResponse() throws InterruptedException {
        log.info("Simulating slow response");
        Thread.sleep(5000L);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "ok");
        response.put("delayMs", 5000);
        response.put("message", "This endpoint intentionally responds slowly for demo purposes.");
        return response;
    }

    @GetMapping("/sensitive-data")
    public Map<String, Object> sensitiveData() {
        return userService.getSensitiveDataSnapshot();
    }

    @GetMapping("/admin/credentials")
    public ResponseEntity<Map<String, Object>> protectedWeakAuthDemo() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "This endpoint is protected by hardcoded Basic Auth credentials.");
        response.put("username", "demo-admin");
        response.put("password", "demo123");
        response.put("warning", "Do not do this outside a training demo.");
        return ResponseEntity.ok(response);
    }
}