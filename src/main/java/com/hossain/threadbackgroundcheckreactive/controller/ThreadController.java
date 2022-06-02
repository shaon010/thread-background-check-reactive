package com.hossain.threadbackgroundcheckreactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class ThreadController {

    /**
     * I'll cancel the request from postman/browser and check if the work still ongoing or not
     */
    @GetMapping(value = "helloThread")
    public Mono<String> helloThread() {
        return Mono.just(true).flatMap(result -> {
            //do some work in background
            System.out.println("Call started");
            return WebClient.builder()
                    .baseUrl("http://localhost:8089/test")
                    .build()
                    .get().uri("/slow-api")
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap((String data) -> {
                        //do some work
                        System.out.println(data);
                        return Mono.just(data);
                    });
        }).doOnSuccess(data -> {
            System.out.println("Success log");
        }).doOnError(data -> {
            System.out.println("Error log");
        }).doOnCancel(() -> {
            System.out.println("Cancel log");
        });
    }
}
