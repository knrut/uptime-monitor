package com.example.uptime.api.mail;

public interface MailSender {
    void sendVerificationCode(String toEmail, String code);
}
