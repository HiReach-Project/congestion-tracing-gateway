package com.hireach.congestiontracinggateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CongestionService {

    @Value("${api.gateway.key}")
    private String apiGatewayKey;

    @Value("${server.ip}")
    private String serverIp;

    private final List<String> nodeUrls = new ArrayList<>() {{
        add("http://" + serverIp + ":8091/hireach/api/congestion"); // HiReach
        add("http://" + serverIp + ":8092/hireach2/api/congestion"); // 2
    }};

    private final WebClient webClient;

    public CongestionService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public int getCongestion(double lat, double lon, double radius, Integer secondsAgo) {

        AtomicInteger congestion = new AtomicInteger(0);

        nodeUrls.parallelStream()
                .forEach((url) -> {
                    Integer nodeCongestion = webClient
                            .get()
                            .uri(url + "?lat=" + lat
                                    + "&lon=" + lon
                                    + "&radius=" + radius
                                    + "&key=" + apiGatewayKey
                            )
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Integer>() {
                            })
                            .block();
                    congestion.set(congestion.get() + nodeCongestion);
                });

        return congestion.get();
    }
}
