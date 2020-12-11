package io.mosip.preregistration.contactus.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailBody {
    private String object;
    private String name;
    private String content;
    private String sign;
}
