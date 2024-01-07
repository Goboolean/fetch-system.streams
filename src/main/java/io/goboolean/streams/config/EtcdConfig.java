package io.goboolean.streams.config;

import io.goboolean.streams.etcd.EtcdClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class EtcdConfig {

    @Value("${etcd.endpoints}")
    private String endpoints;

    @Bean
    public Properties etcdProps() {
        Properties props = new Properties();
        props.put("endpoints", endpoints);
        return props;
    }

    @Bean
    public EtcdClient etcdClient() {
        return new EtcdClient(etcdProps());
    }
}
