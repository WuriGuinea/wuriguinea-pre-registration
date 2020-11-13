package io.mosip.preregistration.contactus.controller;

import io.mosip.preregistration.contactus.models.ContactUsReponseModel;
import io.mosip.preregistration.contactus.models.ContactUsRequestModel;
import io.mosip.preregistration.contactus.models.MainResponseDTO;
import io.mosip.preregistration.contactus.services.HelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/contact-us")
public class ContactUsController {
    private static final Logger log = LoggerFactory.getLogger(ContactUsController.class);

    @Autowired
    private HelperService helperService;

    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public ResponseEntity<MainResponseDTO<ContactUsReponseModel>> sendContactUsData(
            @RequestHeader String sendGridSecretKey,
            @RequestHeader String sendGridEmailId,
            @RequestHeader String emailSubject,
            @RequestBody ContactUsRequestModel request
    ) {
        log.info("Request data received " + request + "\n");
        log.info("sendGridSecretKey " + sendGridSecretKey + "\n");
        log.info("sendGridEmailId " + sendGridEmailId + "\n");
        log.info("emailSubject " + emailSubject + "\n");

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                                this.helperService.contactUsValidatorAndMailSender(
                                        request,
                                        sendGridSecretKey,
                                        sendGridEmailId,
                                        emailSubject
                                )
                );
    }
}
