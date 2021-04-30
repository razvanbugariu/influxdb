package com.bug.influx.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.domain.HealthCheck;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class InfluxDBConfig {

    private final String influxUrl;
    private final char[] influxDbToken;
    private final String organizationName;
    private final String bucketName;

    public InfluxDBConfig(@Value("${influxdb.url}") String influxUrl,
                          @Value("${influxdb.admin.token}") String influxDbToken,
                          @Value("${influxdb.organization}") String organizationName,
                          @Value("${influxdb.bucket}") String bucketName) {
        this.influxUrl = influxUrl;
        this.influxDbToken = influxDbToken.toCharArray();
        this.organizationName = organizationName;
        this.bucketName = bucketName;
    }

    @Bean
    public InfluxDBClient createClient() {
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(influxUrl, influxDbToken, organizationName, bucketName);

        HealthCheck healthCheck = influxDBClient.health();
        if (HealthCheck.StatusEnum.FAIL.equals(healthCheck.getStatus())) {
            throw new BeanCreationException("InfluxDBClient has FAIL HealthCheck.");
        }
        return influxDBClient;
    }
}
