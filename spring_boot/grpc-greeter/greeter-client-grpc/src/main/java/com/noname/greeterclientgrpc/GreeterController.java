package com.noname.greeterclientgrpc;

import com.noname.greeterhellogrpc.GreeterGrpc;
import com.noname.greeterhellogrpc.HelloRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeter")
public class GreeterController {

    private final GreeterGrpc.GreeterBlockingStub greeterStub;

    public GreeterController(GreeterGrpc.GreeterBlockingStub greeterStub) {
        this.greeterStub = greeterStub;
    }

    @GetMapping("/hello")
    public ResponseEntity<HelloResponse> sayHello(@RequestParam String name) {
        var request = HelloRequest.newBuilder()
                .setName(name)
                .build();
        var reply = greeterStub.sayHello(request);

        return ResponseEntity.ok(new HelloResponse(reply.getMessage()));
    }

    public record HelloResponse(String message){}

}
