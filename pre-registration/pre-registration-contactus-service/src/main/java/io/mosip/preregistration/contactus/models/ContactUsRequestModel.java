package io.mosip.preregistration.contactus.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactUsRequestModel {
    private String name;
    private String email;
    private String reason;
    private String sign;
    private String message;
}
