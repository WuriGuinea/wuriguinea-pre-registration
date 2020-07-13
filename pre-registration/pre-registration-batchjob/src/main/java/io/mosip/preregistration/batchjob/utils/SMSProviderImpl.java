package io.mosip.preregistration.batchjob.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import io.mosip.kernel.core.notification.model.SMSResponseDto;
import io.mosip.kernel.core.notification.spi.SMSServiceProvider;

/**
 * @author CONDEIS
 */
@Component
public class SMSProviderImpl implements SMSServiceProvider {

    private static String indicator = "224";
    private static String url = "https://mtnguineevas.com/httpsms/Send";
    private RestTemplate restTemplate;
    private MultiValueMap<String, String> map;
    private HttpHeaders headers;

    public SMSProviderImpl() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        map = new LinkedMultiValueMap<String, String>();
        map.add("client", "wuri");
        map.add("password", "wurid20");
        map.add("affiliate", "999");
        map.add("from", "WURI-GUINEE");
    }

    /**
     * @param contactNumber
     * @param message
     * @return
     */
    @Override
    public SMSResponseDto sendSms(String contactNumber, String message) {
        map.add("phone", indicator + contactNumber);
        map.add("text", message + "Généré à:" + System.currentTimeMillis());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        SMSResponseDto response = new SMSResponseDto();
        response.setStatus(responseEntity.getBody());
        return response;
    }
}
