package io.mosip.preregistration.contactus.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactUsReponseModel {
    //TODO to be include in core
    private int errorCode;
    private String description;
}
