package com.luyu.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pharma.email")
@Data
public class EmailProperties {
    public String from;
    public String to;
}
