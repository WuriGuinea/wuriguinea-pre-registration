package io.mosip.preregistrationcontacus.preregistrationcontactusservice.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sendgrid")
@Data
public class SendGridConfig {

    private String apiKey;
}
