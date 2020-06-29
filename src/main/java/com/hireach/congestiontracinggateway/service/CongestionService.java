package com.hireach.congestiontracinggateway.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CongestionService {

    private final HashMap<String, String> nodeUrls = new HashMap<>() {{
        put("http://localhost:8091/api/congestion", "HM4fQAQmLF36LCWEctCuJrEtqTtgegpG"); // HiReach
        put("http://localhost:8092/api/congestion", "HhPafhF9dwA3gC77PsdwrDFtydkYsnCc"); // UPB
    }};

    private final WebClient webClient;

    public CongestionService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public int getCongestion(double lat, double lon, double radius, Integer secondsAgo) {

        // Get all nodes -> foreach -> get congestion -> sum
        AtomicInteger congestion = new AtomicInteger(0);

        nodeUrls.forEach((url, pass) -> {
            Integer nodeCongestion = webClient
                    .get()
                    .uri(url + "?lat=" + lat
                            + "&lon=" + lon
                            + "&radius=" + radius
                            + "&key=" + pass
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
