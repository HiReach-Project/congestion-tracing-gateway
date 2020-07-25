package com.hireach.congestiontracinggateway.controller;

import com.hireach.congestiontracinggateway.model.NodeModel;
import com.hireach.congestiontracinggateway.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NodeController {

    private final NodeRepository nodeRepository;

    @GetMapping(value = "/nodes")
    @ResponseStatus(HttpStatus.OK)
    public List<NodeModel> getNodes() {
        return nodeRepository.findAll()
                .stream()
                .map(node -> NodeModel.builder()
                        .name(node.getName())
                        .url(node.getUrl())
                        .build())
                .collect(Collectors.toList());
    }

}

