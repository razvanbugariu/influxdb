package com.bug.influx.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class InfluxDBConfig {

//    private final String influxUrl;
//    private final char[] influxDbToken;
//    private final String organizationName;
//    private final String bucketName;
//
//    public InfluxDBConfig(@Value("${influxdb.url}") String influxUrl,
//                          @Value("${influxdb.admin.token}") String token,
//                          @Value("${influxdb.organization}") String organizationName,
//                          @Value("${influxdb.bucket}") String bucketName) {
//        this.influxUrl = influxUrl;
//        this.influxDbToken = token.toCharArray();
//        this.organizationName = organizationName;
//        this.bucketName = bucketName;
//    }

    private final String influxUrl;
    private final String username;
    private final String password;

    public InfluxDBConfig(@Value("${influxdb.url}") String influxUrl,
                          @Value("${influxdb.username}") String username,
                          @Value("${influxdb.password}") String password) {
        this.influxUrl = influxUrl;
        this.username = username;
        this.password = password;
    }

    @Bean
    public InfluxDB createClient() {
        InfluxDB influxDB = InfluxDBFactory.connect(influxUrl, username, password);

        Pong pong = influxDB.ping();
        if (!pong.isGood()) {
            throw new BeanCreationException("InfluxDB Pong is bad!");
        }
        String databaseName = "events";
        influxDB.query(new Query("CREATE DATABASE " + databaseName));
        influxDB.setDatabase(databaseName);
        return influxDB;
    }
}
