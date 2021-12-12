package ru.labs.grading;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class ByeServiceImpl extends ByeServiceGrpc.ByeServiceImplBase {

    @Override
    public void byeGreeting(ByeServiceOuterClass.ByeRequest request,
                            StreamObserver<ByeServiceOuterClass.ByeResponse> responseObserver) {
        System.out.println(request);

        //ответ клиенту
        ByeServiceOuterClass.ByeResponse response =
                ByeServiceOuterClass.ByeResponse
                        .newBuilder()
                        .setByeGreeting("Bye from server " + request.getByeName())
                        .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();


    }
}
