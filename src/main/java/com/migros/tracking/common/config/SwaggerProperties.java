package com.migros.tracking.common.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties("swagger")
@PropertySource("classpath:swagger-default.properties")
public class SwaggerProperties {

    private String apiPath;

    private boolean corsEnabled;

    private Info info;

    private Contact contact;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Info {

        private String title;

        private String description;

        private String version;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Contact {

        private String name;

        private String url;

        private String email;

    }

}