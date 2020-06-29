package com.hireach.congestiontracinggateway;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA3_256;

//@SpringBootTest
class CongestionTracingGatewayApplicationTests {

    @Test
    @Rollback(value = false)
    void contextLoads() {

    }

}
