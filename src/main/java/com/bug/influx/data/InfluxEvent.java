package com.bug.influx.data;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = "event")
@Data
@NoArgsConstructor
public class InfluxEvent {

    @Column(tag = true)
    private String source;

    @Column
    private String value;

    @Column(timestamp = true)
    private Instant time;
}
