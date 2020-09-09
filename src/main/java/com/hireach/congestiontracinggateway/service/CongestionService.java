//    Congestion API - a REST API built to track congestion spots and
//    crowded areas using real-time location data from mobile devices.
//
//    Copyright (C) 2020, University Politehnica of Bucharest, member
//    of the HiReach Project consortium <https://hireach-project.eu/>
//    <andrei[dot]gheorghiu[at]upb[dot]ro. This project has received
//    funding from the European Union’s Horizon 2020 research and
//    innovation programme under grant agreement no. 769819.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
