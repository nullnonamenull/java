package com.noname.consumerone;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consumerone")
public class TestController {

    private final TestInMemoryService testInMemoryService;

    public TestController(TestInMemoryService testInMemoryService) {
        this.testInMemoryService = testInMemoryService;
    }

    @GetMapping
    public ResponseEntity<List<SomeDto>> getAll() {
        return ResponseEntity.ok(testInMemoryService.getAll());
    }

}
