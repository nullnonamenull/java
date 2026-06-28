package com.noname.greeterhellogrpc;

import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import com.noname.greeterhellogrpc.GreeterGrpc;
import com.noname.greeterhellogrpc.HelloRequest;
import com.noname.greeterhellogrpc.HelloReply;

@GrpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase{

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        var message = "Hello %s".formatted(request.getName());
        var reply = HelloReply.newBuilder().setMessage(message).build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
