package com.hellokoding.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private final String from;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void sendMail(String to, String subject, String bodyText, String attachmentPath) throws MessagingException {
        send(from, to, subject, bodyText, attachmentPath, !Optional.ofNullable(attachmentPath).orElse("").isEmpty());
    }

    private void send(String from, String to, String subject, String bodyText, String attachmentPath, boolean multipart) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(bodyText);

        if (!Optional.ofNullable(attachmentPath).orElse("").isEmpty()) {
            FileSystemResource file = new FileSystemResource(attachmentPath);
            helper.addAttachment(file.getFilename(), file);
        }
        mailSender.send(message);
    }
}
