package com.project.barberreservation.service;

import jakarta.mail.MessagingException;

public interface IEmailService {
    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException;
}
