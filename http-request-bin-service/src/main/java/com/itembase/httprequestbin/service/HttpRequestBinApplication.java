package com.itembase.httprequestbin.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
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
            request -> request
                .bodyToMono(String.class)
                .switchIfEmpty(Mono.just(""))
                .flatMap(body -> {
                    log.info("-------------------------- Data begin -------------------------");
                    log.info(request.method().name());
                    log.info(request.path());

                    request
                        .headers()
                        .asHttpHeaders()
                        .toSingleValueMap()
                        .forEach((key, value) ->
                            log.info("Header={}, Value={}", key, value)
                        );

                    log.info(body);
                    log.info("--------------------------- Data ends -------------------------");

                    return ServerResponse.ok().build();
                })
        );
    }
}
