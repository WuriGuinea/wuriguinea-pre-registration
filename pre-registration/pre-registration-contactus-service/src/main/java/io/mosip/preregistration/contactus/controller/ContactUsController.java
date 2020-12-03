package io.mosip.preregistration.contactus.controller;

import io.mosip.preregistration.contactus.models.*;
import io.mosip.preregistration.contactus.services.CaptchaService;
import io.mosip.preregistration.contactus.services.HelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping("/contact-us")
public class ContactUsController {
    private static final Logger log = LoggerFactory.getLogger(ContactUsController.class);

    @Autowired
    private HelperService helperService;

    @Autowired
    private CaptchaService captchaService;

    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public ResponseEntity<MainResponseDTO<ContactUsReponseModel>> sendContactUsData(
            @RequestBody ContactUsRequestModel request
    ) {
        log.info("Request data received from forms");

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                                this.helperService.contactUsValidatorAndMailSender(request)
                );
    }

    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"},
            value = "/captcha"
    )
    public ResponseEntity<MainResponseDTO<RecaptchaResponse>> googleCaptchaVerification(
            @RequestHeader String token
    ) {
        MainResponseDTO<RecaptchaResponse> mainResponseDTO = new MainResponseDTO<>();


            mainResponseDTO.setId(UUID.randomUUID().toString());
            mainResponseDTO.setResponsetime(new Date().toInstant().toString());
            mainResponseDTO.setVersion("V1");
            mainResponseDTO.setResponse(null);
            mainResponseDTO.setResponse(captchaService.verify(token));



      return  new ResponseEntity<>(mainResponseDTO, HttpStatus.ACCEPTED);
    }
}
