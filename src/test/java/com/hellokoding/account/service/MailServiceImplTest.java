package com.hellokoding.account.service;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class MailServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImplTest.class);
    private static final String HEADER_KEY_4_TO = "To";
    private static final String HEADER_KEY_4_SUBJECT = "Subject";

    @SuppressWarnings("unchecked")
    @Test
    public void testSendMessage() throws Exception {
        String smtpHost = "localhost";
        int smtpPort = 12345;

        String subject = "MY_SUBJECT";
        String messageBody = "MY_MESSAGE_BODY";
        String fromAddr = "sender@localhost";
        String toAddr = "test@test.com";

        logger.info("Start dumbster fake SMTP server...");
        SimpleSmtpServer server = SimpleSmtpServer.start(12345);

        logger.info("Send mail message");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", smtpHost);
        properties.setProperty("mail.smtp.port", String.valueOf(smtpPort));
        mailSender.setJavaMailProperties(properties);
        MailServiceImpl mailService = new MailServiceImpl(mailSender, fromAddr);

        mailService.sendMail(toAddr, subject, messageBody, null);
        server.stop();

        assertEquals(1, server.getReceivedEmails().size());

        SmtpMessage email = server.getReceivedEmails().get(0);

        assertEquals(toAddr, email.getHeaderValue(HEADER_KEY_4_TO));
        assertEquals(subject, email.getHeaderValue(HEADER_KEY_4_SUBJECT));
        assertEquals(messageBody, email.getBody());
    }

}