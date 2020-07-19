package com.hireach.congestiontracinggateway.controller;

import com.hireach.congestiontracinggateway.service.CongestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class CongestionController {

    private final CongestionService congestionService;

    @GetMapping(value = "/congestion")
    @ResponseStatus(HttpStatus.OK)
    public int getCongestion(@RequestParam("lat") @DecimalMin("-90.0") @DecimalMax("90.0") @Digits(integer = 2, fraction = 8) double lat,
                             @RequestParam("lon") @DecimalMin("-180.0") @DecimalMax("180.0") @Digits(integer = 3, fraction = 8) double lon,
                             @RequestParam("radius") @DecimalMin(value = "0.0", inclusive = false) @DecimalMax("6378000") double radius,
                             @RequestParam(value = "seconds_ago", required = false) Integer secondsAgo) {
        return congestionService.getCongestion(lat, lon, radius, secondsAgo);
    }

}

