package io.mosip.preregistrationcontacus.preregistrationcontactusservice.services;

import io.mosip.preregistrationcontacus.preregistrationcontactusservice.models.ContactUsReponseModel;
import io.mosip.preregistrationcontacus.preregistrationcontactusservice.models.ContactUsRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelperService {
    @Autowired
    private SendGridEmailService sendGridEmailService;

    public ContactUsReponseModel contactUsDataValidator(
            ContactUsRequestModel request
    ) {
        System.out.println(request);
        if (request.getName() == null) {
            return this.responseBuilder(400, "BAD REQUEST, name is mandatory, please fill it");
        }
        else if (request.getEmail() == null) {
            return this.responseBuilder(400, "BAD REQUEST, email is mandatory, please fill it");
        }
        else if (request.getReason() == null) {
            return this.responseBuilder(400, "BAD REQUEST, reason is mandatory, please fill it");
        }
        else if (request.getComment() == null) {
            return this.responseBuilder(400, "BAD REQUEST, comment is mandatory, please fill it");
        }

        if (
                this.sendGridEmailService.sendEmailToUser(
                                request.getEmail(),
                           "Nous avons bien reçu votre demande, vous serez contacter bientôt."
                )
        ) {
            return this.responseBuilder(201, "SUCCESS");
        }

        return this.responseBuilder(500, "INTERNAL SERVER ERROR, please contact you admin");

    }

    public ContactUsReponseModel responseBuilder(int status, String desc) {
      return   ContactUsReponseModel
                .builder()
                .errorCode(status)
                .description(desc)
                .build();
    }
}
