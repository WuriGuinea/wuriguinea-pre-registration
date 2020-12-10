package io.mosip.preregistration.contactus.services;

import io.mosip.preregistration.contactus.models.RecaptchaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@PropertySource("classpath:application.properties")
@Service
public class CaptchaService {

    private final RestTemplate restTemplate;

    public CaptchaService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String recaptchaSecret = System.getenv("GOOGLE_CAPTCHA_KEY");

    @Value("${google.recaptcha.verify.url}")
    public String recaptchaVerifyUrl;

    public RecaptchaResponse verify(String response) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("secret", recaptchaSecret);
        param.add("response", response);

        try {
            return this.restTemplate.postForObject(recaptchaVerifyUrl, param, RecaptchaResponse.class);
        } catch (RestClientException e) {
            System.out.print(e.getMessage());
        }

        return null;
    }
}
