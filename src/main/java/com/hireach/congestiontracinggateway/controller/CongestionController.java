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

