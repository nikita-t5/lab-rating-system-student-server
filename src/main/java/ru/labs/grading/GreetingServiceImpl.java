package ru.labs.grading;

import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase{

    @Override
    public void greeting(GreetingServiceOuterClass.HelloRequest request,
                         StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver){
        System.out.println(request);

        //ответ клиенту
        GreetingServiceOuterClass.HelloResponse response =
                GreetingServiceOuterClass.HelloResponse
                        .newBuilder().setGreeting("Hello from server, " + request.getName())
                        .build();

        //отправить ответ клиенту. один ответ
        responseObserver.onNext(response);

        //закрыть поток
        responseObserver.onCompleted();
    }
}
