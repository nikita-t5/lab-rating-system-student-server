package ru.labs.grading;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
        System.out.println("hi");
//        Server server = ServerBuilder.forPort(8090)
//                .addService(new GreetingServiceImpl())
//                .build();
//        server.start();
//        System.out.println("server started");
//        server.awaitTermination();
    }
}
