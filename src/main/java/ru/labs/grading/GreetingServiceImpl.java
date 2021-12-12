package ru.labs.grading;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

//TODO: GRpcService оформить аналогично rest controller
@GRpcService //Эта аннотация помечает, что класс должен быть зарегистрирован как gRPC бин.
public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void greeting(GreetingServiceOuterClass.HelloRequest request,
                         StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {
        System.out.println(request);

        //ответ клиенту
        GreetingServiceOuterClass.HelloResponse response =
                GreetingServiceOuterClass.HelloResponse
                        .newBuilder()
                        .setGreeting("Hello from server, " + request.getName())
                        .build();

        //отправить ответ клиенту. один ответ
        responseObserver.onNext(response);

        //закрыть поток. отправить уведомление об успешном завершении стрима.
        responseObserver.onCompleted();
    }
}
