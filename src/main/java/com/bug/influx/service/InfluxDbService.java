package com.bug.influx.service;

import com.bug.influx.data.InfluxEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class InfluxDbService {

    private final InfluxDBClient influxDBClient;
    private final String bucketName;
    private final ObjectMapper objectMapper;

    public InfluxDbService(InfluxDBClient influxDBClient, @Value("${influxdb.bucket}") String bucketName) {
        this.influxDBClient = influxDBClient;
        this.bucketName = bucketName;
        this.objectMapper = new ObjectMapper();
    }

    public void write(String source, String price) {
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            InfluxEvent event = new InfluxEvent();
            event.setSource(source);
            event.setValue(price);

            writeApi.writeMeasurement(WritePrecision.MS, event);
        }
    }

    public String getAll(Long start, Long stop) throws JsonProcessingException {
        String fluxQuery = String.format("from(bucket:\"%s\") |> range(start: %d, stop: %d)", bucketName, start, stop);
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<InfluxEvent> events = queryApi.query(fluxQuery, InfluxEvent.class);
        return objectMapper.writeValueAsString(events);
    }
}
