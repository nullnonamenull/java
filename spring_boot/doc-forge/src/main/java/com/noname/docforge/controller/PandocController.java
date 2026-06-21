package com.noname.docforge.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
//@RequestMapping("/pandoc")
public class PandocController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping(
            value = "/markdown-to-html",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public ResponseEntity<String> markdownToHtml(@RequestBody String markdown) {

        try {
            Path input = Files.createTempFile("pandoc-input-", ".md");
            Path output = Files.createTempFile("pandoc-outuput-", ".html");

            Files.writeString(input, markdown);

            Process process = new ProcessBuilder(
                    "pandoc",
                    input.toAbsolutePath().toString(),
                    "-f", "markdown",
                    "-t", "html",
                    "-o", output.toAbsolutePath().toString()
            )
                    .redirectErrorStream(true)
                    .start();

            String logs = new String(process.getInputStream().readAllBytes());

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Pandoc failed: " + logs);
            }

            return ResponseEntity.ok(Files.readString(output));
        } catch (Exception e) {
            throw new RuntimeException("Pandoc failed: ", e);
        }
    }

}
