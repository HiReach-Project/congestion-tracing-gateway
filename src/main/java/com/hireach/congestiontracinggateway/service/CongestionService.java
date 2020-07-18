package com.hireach.congestiontracinggateway.service;

import com.hireach.congestiontracinggateway.entity.Node;
import com.hireach.congestiontracinggateway.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CongestionService {

    @Value("${api.gateway.key}")
    private String apiGatewayKey;

    private final NodeRepository nodeRepository;

    private final WebClient.Builder webClientBuilder;

    public int getCongestion(double lat, double lon, double radius, Integer secondsAgo) {
        List<String> nodeUrls = nodeRepository.findAll()
                .stream()
                .map(Node::getUrl)
                .collect(Collectors.toList());

        AtomicInteger congestion = new AtomicInteger(0);

        nodeUrls.parallelStream()
                .forEach((url) -> {
                    try {
                        Integer nodeCongestion = webClientBuilder
                                .build()
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
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });

        return congestion.get();
    }
}
