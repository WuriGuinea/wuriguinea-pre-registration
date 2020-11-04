package io.mosip.preregistrationcontacus.preregistrationcontactusservice.services;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.mosip.preregistrationcontacus.preregistrationcontactusservice.configs.SendGridConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService {
    @Autowired
    private SendGridConfig cf;

    public boolean sendEmailToUser(String email, String body) {
        Email from = new Email("myaya.diallo@wuriguinee.com");
        String subject = "WURI TEST CONTACT US";
        Email to = new Email(email);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(this.cf.getApiKey());
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
