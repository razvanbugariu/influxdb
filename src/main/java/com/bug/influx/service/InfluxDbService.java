package com.bug.influx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class InfluxDbService {

    private final InfluxDB influxDBClient;
    private final ObjectMapper objectMapper;

    public InfluxDbService(InfluxDB influxDBClient) {
        this.influxDBClient = influxDBClient;
        this.objectMapper = new ObjectMapper();
    }

    public void write(String source, String price) {
        influxDBClient.setDatabase("events");
        influxDBClient.write(Point.measurement("event")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("source", source)
                .addField("price", Double.valueOf(price))
                .build());
    }

    public String getAll(Long start, Long stop) throws JsonProcessingException {
        QueryResult query = influxDBClient.query(new Query("SELECT * FROM event"));
        List<QueryResult.Result> results = query.getResults();
        return objectMapper.writeValueAsString(results);
    }
}
