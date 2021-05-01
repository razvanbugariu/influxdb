package com.bug.influx.controller;

import com.bug.influx.service.InfluxDbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppController {

    private final InfluxDbService influxDbService;

    public AppController(InfluxDbService influxDbService) {
        this.influxDbService = influxDbService;
    }

    @GetMapping
    public String getAll(@RequestParam(value = "start", required = false) Long start,
                         @RequestParam(value = "stop", required = false) Long stop) throws JsonProcessingException {
        return influxDbService.getAll(start, stop);
    }

    @PostMapping("/{price}")
    public void write(@PathVariable("price") String price) {
        influxDbService.write("POST", price);
    }
}
