package io.mosip.preregistration.contactus.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailBody {
    private String object;
    private String name;
    private String content;
    private String sign;
}
