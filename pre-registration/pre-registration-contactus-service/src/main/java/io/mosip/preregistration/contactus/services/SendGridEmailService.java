package io.mosip.preregistration.contactus.services;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.mosip.preregistration.contactus.configs.SendGridConfig;
import io.mosip.preregistration.contactus.models.ContactUsReponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService {
    @Autowired
    private SendGridConfig cf;

    public ContactUsReponseModel sendEmailToUser(String email, String body) {
        Email from = new Email("myaya.diallo@wuriguinee.com");
        String subject = "WURI TEST CONTACT US";
        Email to = new Email(email);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        String apiKey = this.cf.getApiKey();

        if (apiKey.length() == 0) {
            apiKey = System.getenv("SENDRIG_API_KEY");
        }
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() != 202) {
                return ContactUsReponseModel.builder()
                        .errorCode(response.getStatusCode())
                        .description(response.getBody())
                        .build();
            }

            return ContactUsReponseModel.builder()
                    .errorCode(201)
                    .description("Email succesfully sent to " + email)
                    .build();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ContactUsReponseModel.builder()
                    .errorCode(500)
                    .description("INTERNAL SERVER ERROR " + email)
                    .build();
        }
    }
}
