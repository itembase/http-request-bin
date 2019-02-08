package com.itembase.httprequestbin.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@Configuration
public class HttpRequestBinApplication {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HttpRequestBinApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HttpRequestBinApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> importRouter() {
        return route(
            path("/**"),
            request -> {
                final var contentType = request.headers().contentType();

                if (contentType.isPresent() && contentType.get().equals(MediaType.APPLICATION_FORM_URLENCODED)) {
                    log.info("-------------------------- Data begin -------------------------");

                    printRequest(request);

                    return request
                        .formData()
                        .flatMap(formData -> {
                            formData
                                .keySet()
                                .forEach(key -> formData
                                    .get(key)
                                    .forEach(value -> log.info("Key={}, Value={}", key, value))
                                );

                            log.info("--------------------------- Data ends -------------------------");

                            return Mono.empty();
                        })
                        .then(ServerResponse.ok().build());
                }

                return request
                    .bodyToMono(String.class)
                    .switchIfEmpty(Mono.just(""))
                    .flatMap(body -> {
                        log.info("--------------------------- Data begin -------------------------");

                        printRequest(request);

                        log.info(body);
                        log.info("--------------------------- Data ends -------------------------");

                        return ServerResponse.ok().build();
                    });
            }
        );
    }

    private void printRequest(ServerRequest request) {
        log.info(request.method().name());
        log.info(request.path());

        request
            .headers()
            .asHttpHeaders()
            .toSingleValueMap()
            .forEach((key, value) ->
                log.info("Header={}, Value={}", key, value)
            );
    }
}
