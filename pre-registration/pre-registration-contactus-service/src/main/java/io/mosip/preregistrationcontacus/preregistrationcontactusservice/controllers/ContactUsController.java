package io.mosip.preregistrationcontacus.preregistrationcontactusservice.controllers;

import io.mosip.preregistrationcontacus.preregistrationcontactusservice.models.ContactUsReponseModel;
import io.mosip.preregistrationcontacus.preregistrationcontactusservice.models.ContactUsRequestModel;
import io.mosip.preregistrationcontacus.preregistrationcontactusservice.services.HelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public ResponseEntity<ContactUsReponseModel> sendContactUsData(
            @RequestBody ContactUsRequestModel request
    ) {
        log.info("Request data received from forms" );
        log.info(request.toString());

        return new ResponseEntity<>(
                    this.helperService.contactUsDataValidator(request)
                ,
                HttpStatus.OK
        );
    }
}
