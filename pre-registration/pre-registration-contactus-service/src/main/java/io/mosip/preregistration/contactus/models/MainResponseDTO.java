package io.mosip.preregistration.contactus.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//TODO to be integration in the CORE
public class MainResponseDTO<T> implements Serializable {
    private static final long serialVersionUID = 3384945682672832638L;
    @ApiModelProperty(
            value = "request id",
            position = 1
    )
    private String id;
    @ApiModelProperty(
            value = "request version",
            position = 2
    )
    private String version;
    @ApiModelProperty(
            value = "Response Time",
            position = 3
    )
    private String responsetime;
    @ApiModelProperty(
            value = "Response",
            position = 4
    )
    private T response;
    @ApiModelProperty(
            value = "Error Details",
            position = 5
    )
    private List<ExceptionJSONInfoDTO> errors;
}
