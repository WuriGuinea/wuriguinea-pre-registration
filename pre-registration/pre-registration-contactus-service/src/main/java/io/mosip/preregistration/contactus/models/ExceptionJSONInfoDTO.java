package io.mosip.preregistration.contactus.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionJSONInfoDTO implements Serializable {

    private static final long serialVersionUID = -4870610344003366727L;

    private String errorCode;
    private String message;
}
