package io.mosip.preregistrationcontacus.preregistrationcontactusservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactUsReponseModel {
    private int errorCode;
    private String description;
}
