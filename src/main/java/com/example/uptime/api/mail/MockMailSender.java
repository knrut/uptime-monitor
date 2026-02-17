package com.example.uptime.api.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "mail.mock", havingValue = "true", matchIfMissing = true)
public class MockMailSender implements MailSender {

    private static final Logger log = LoggerFactory.getLogger(MockMailSender.class);

    @Override
    public void sendVerificationCode(String toEmail, String code) {
        log.info("[MOCK MAIL] to={} verificationCode={}", toEmail, code);
    }
}
