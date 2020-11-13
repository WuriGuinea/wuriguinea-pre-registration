package io.mosip.preregistration.contactus.services;


import io.mosip.preregistration.contactus.models.ContactUsReponseModel;
import io.mosip.preregistration.contactus.models.ContactUsRequestModel;
import io.mosip.preregistration.contactus.models.MainResponseDTO;
import io.mosip.preregistration.contactus.models.ExceptionJSONInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class HelperService {
    @Autowired
    private SendGridEmailService sendGridEmailService;

    public MainResponseDTO<ContactUsReponseModel> contactUsValidatorAndMailSender(
            ContactUsRequestModel request,
            String sendGridSecretKey,
            String sendGridEmailId,
            String emailSubject
    ) {
        ContactUsReponseModel valideData = contactDataValidator(request);

        List<ExceptionJSONInfoDTO> errors = new ArrayList<>();

        MainResponseDTO<ContactUsReponseModel> mainResponseDTO = new MainResponseDTO<>();

        mainResponseDTO.setId(UUID.randomUUID().toString());
        mainResponseDTO.setResponsetime(new Date().toInstant().toString());
        mainResponseDTO.setVersion("V1");


        if (valideData.getErrorCode() == 400) {
            errors.add(new ExceptionJSONInfoDTO("400", valideData.getDescription()));
            mainResponseDTO.setErrors(errors);
            return mainResponseDTO;
        }

        ContactUsReponseModel contactUsReponseModel = this.sendGridEmailService.sendEmailToUser(
                request.getEmail(),
                "Nous avons bien reçu votre demande, vous serez contacter bientôt.",
                sendGridSecretKey,
                sendGridEmailId,
                emailSubject
        );

        if (contactUsReponseModel.getErrorCode() != 202) {
            errors.add(
                    new ExceptionJSONInfoDTO(
                            String.valueOf(contactUsReponseModel.getErrorCode()),
                            valideData.getDescription()
                    )
            );
        }

        mainResponseDTO.setResponse(contactUsReponseModel);

        return mainResponseDTO;

    }

    private ContactUsReponseModel contactDataValidator(ContactUsRequestModel request) {
        if (request.getName() == null) {
            return this.responseBuilder(400, "BAD REQUEST, name is mandatory, please fill it");
        } else if (request.getEmail() == null) {
            return this.responseBuilder(400, "BAD REQUEST, email is mandatory, please fill it");
        } else if (request.getReason() == null) {
            return this.responseBuilder(400, "BAD REQUEST, reason is mandatory, please fill it");
        } else if (request.getMessage() == null) {
            return this.responseBuilder(400, "BAD REQUEST, comment is mandatory, please fill it");
        }

        return this.responseBuilder(201, "SUCCESS");
    }

    public ContactUsReponseModel responseBuilder(int status, String desc) {
      return   ContactUsReponseModel
                .builder()
                .errorCode(status)
                .description(desc)
                .build();
    }
}
