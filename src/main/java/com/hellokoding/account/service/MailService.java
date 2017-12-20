package com.hellokoding.account.service;

import javax.mail.MessagingException;

public interface MailService {
    void sendMail(String to, String subject, String bodyText, String attachmentPath) throws MessagingException;
}
