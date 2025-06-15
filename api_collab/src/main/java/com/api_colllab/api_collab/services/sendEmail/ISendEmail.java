package com.api_colllab.api_collab.services.sendEmail;

import com.api_colllab.api_collab.controller.dto.EmailRequest;

public interface ISendEmail {

    void sendEmail(EmailRequest emailRequest);

}
