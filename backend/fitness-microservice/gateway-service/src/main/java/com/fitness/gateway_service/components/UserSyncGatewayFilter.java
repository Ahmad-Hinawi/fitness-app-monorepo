package com.fitness.gateway_service.components;

import com.fitness.gateway_service.user.RequestUser;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserSyncGatewayFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;
    private final Set<String> syncUsers= ConcurrentHashMap.newKeySet();

    public UserSyncGatewayFilter(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // ✅ 1. Skip irrelevant requests (VERY IMPORTANT)
        if (path.contains("favicon") || path.contains("actuator")) {
            return chain.filter(exchange);
        }

        return exchange.getPrincipal()
                .filter(p -> p instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .flatMap(jwtAuth -> {

                    var jwt = jwtAuth.getToken();

                    String keycloakId = jwt.getSubject();
                    String email = jwt.getClaimAsString("email");
                    String firstname = jwt.getClaimAsString("given_name");
                    String lastname = jwt.getClaimAsString("family_name");

                    // ✅ 2. Validate required data (safety)
                    if (keycloakId == null || email == null) {
                        return chain.filter(exchange);
                    }
                    if(syncUsers.contains(keycloakId)){
                        return chain.filter(exchange);
                    }
                    syncUsers.add(keycloakId);

                    System.out.println("GATEWAY: Sync attempt -> " + email);

                    RequestUser body = new RequestUser(
                            keycloakId,
                            "password123", // ⚠️ consider removing (see notes)
                            email,
                            firstname,
                            lastname
                    );

                    return webClient.post()
                            .uri("http://USER-SERVICE/api/users/sync")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body)
                            .retrieve()
                            .toBodilessEntity()

                            // ✅ 3. Don't break request flow if sync fails
                            .doOnSuccess(res ->
                                    System.out.println("GATEWAY: Sync OK -> " + email))

                            .doOnError(err ->

                                    System.err.println("GATEWAY: Sync FAILED -> " + err.getMessage()))

                            .onErrorResume(err ->{ syncUsers.remove(keycloakId); return Mono.empty();}) // swallow error

                            .then(chain.filter(exchange));
                })

                // ✅ 4. No auth → just continue silently (clean logs)
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return 1; // ✅ run AFTER security
    }
}