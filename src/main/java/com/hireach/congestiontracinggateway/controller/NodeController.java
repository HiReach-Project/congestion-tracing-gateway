package com.hireach.congestiontracinggateway.controller;

import com.hireach.congestiontracinggateway.entity.Node;
import com.hireach.congestiontracinggateway.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NodeController {

    private final NodeRepository nodeRepository;

    @GetMapping(value = "/nodes")
    @ResponseStatus(HttpStatus.OK)
    public List<Node> getNodes() {
        return nodeRepository.findAll();
    }

}

