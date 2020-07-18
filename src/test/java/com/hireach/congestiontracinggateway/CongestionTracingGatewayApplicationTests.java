package com.hireach.congestiontracinggateway;

import com.hireach.congestiontracinggateway.entity.Node;
import com.hireach.congestiontracinggateway.repository.NodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class CongestionTracingGatewayApplicationTests {

    @Autowired
    NodeRepository nodeRepository;

    @Test
    @Rollback(value = false)
    void contextLoads() {
        Node node = new Node();
        node.setName("name");
        node.setUrl("rl");
        nodeRepository.save(node);
    }

}
