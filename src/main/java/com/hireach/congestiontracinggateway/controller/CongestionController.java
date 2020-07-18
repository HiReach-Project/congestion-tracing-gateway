package com.hireach.congestiontracinggateway.controller;

import com.hireach.congestiontracinggateway.service.CongestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CongestionController {

    private final CongestionService congestionService;

    @GetMapping(value = "/congestion")
    @ResponseStatus(HttpStatus.OK)
    public int getCongestion(@RequestParam("lat") double lat,
                             @RequestParam(value = "lon") double lon,
                             @RequestParam(value = "radius") double radius,
                             @RequestParam(value = "seconds_ago", required = false) Integer secondsAgo) {
        return congestionService.getCongestion(lat, lon, radius, secondsAgo);
    }

}
