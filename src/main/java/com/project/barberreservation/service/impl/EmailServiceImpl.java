package com.project.barberreservation.service.impl;

import com.project.barberreservation.service.IEmailService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from.email}")
    private String fromEmail;

    private Resend resend;

    @PostConstruct
    public void init() {
        this.resend = new Resend(resendApiKey);
        log.info("Resend client initialized with from email: {}", fromEmail);
    }

    private String validateAndFormatEmail(String email, String fieldName) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException(fieldName + " cannot be null or empty");
        }

        String trimmedEmail = email.trim();

        // Email formatını kontrol et: ya sadece email ya da "İsim <email>" formatında olmalı
        if (!trimmedEmail.matches("^[^<]+<[^>]+>$") && !trimmedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            log.warn("Invalid email format for {}: {}. Attempting to fix...", fieldName, trimmedEmail);

            // Eğer isim ve email karışmışsa, sadece email kısmını al
            if (trimmedEmail.contains("<") && trimmedEmail.contains(">")) {
                int start = trimmedEmail.indexOf("<") + 1;
                int end = trimmedEmail.indexOf(">");
                if (start < end) {
                    trimmedEmail = trimmedEmail.substring(start, end).trim();
                }
            }

            // Geçerli bir email adresi mi kontrol et
            if (!trimmedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new RuntimeException("Invalid email format for " + fieldName + ": " + email);
            }
        }

        return trimmedEmail;
    }

    @Override
    public void sendVerificationEmail(String toEmail, String subject, String htmlContent) {
        try {
            String formattedFrom = validateAndFormatEmail(fromEmail, "fromEmail");
            String formattedTo = validateAndFormatEmail(toEmail, "toEmail");

            log.info("Sending verification email from: {} to: {}", formattedFrom, formattedTo);

            SendEmailRequest request = SendEmailRequest.builder()
                    .from(formattedFrom)
                    .to(formattedTo)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            SendEmailResponse response = resend.emails().send(request);
            log.info("Verification email sent successfully to: {}, Message ID: {}", toEmail, response.getId());
        } catch (ResendException e) {
            log.error("Failed to send verification email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send verification email: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            log.error("Email validation error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            String formattedFrom = validateAndFormatEmail(fromEmail, "fromEmail");
            String formattedTo = validateAndFormatEmail(toEmail, "toEmail");

            String subject = "Password Reset Request";
            String htmlContent = "<html>"
                    + "<body style=\"font-family: Arial, sans-serif;\">"
                    + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                    + "<h2 style=\"color: #333;\">Password Reset</h2>"
                    + "<p style=\"font-size: 16px;\">Click the link below to reset your password:</p>"
                    + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                    + "<a href=\"https://yourapp.com/reset-password?token=" + resetToken + "\" style=\"background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;\">Reset Password</a>"
                    + "</div>"
                    + "<p style=\"font-size: 14px; color: #666;\">This link will expire in 1 hour.</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            SendEmailRequest request = SendEmailRequest.builder()
                    .from(formattedFrom)
                    .to(formattedTo)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            SendEmailResponse response = resend.emails().send(request);
            log.info("Password reset email sent successfully to: {}, Message ID: {}", toEmail, response.getId());
        } catch (ResendException e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            log.error("Email validation error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void sendAppointmentConfirmation(String toEmail, String appointmentDetails) {
        try {
            String formattedFrom = validateAndFormatEmail(fromEmail, "fromEmail");
            String formattedTo = validateAndFormatEmail(toEmail, "toEmail");

            String subject = "Appointment Confirmation";
            String htmlContent = "<html>"
                    + "<body style=\"font-family: Arial, sans-serif;\">"
                    + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                    + "<h2 style=\"color: #333;\">Appointment Confirmed</h2>"
                    + "<p style=\"font-size: 16px;\">Your appointment details:</p>"
                    + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                    + appointmentDetails
                    + "</div>"
                    + "<p style=\"font-size: 14px; color: #666;\">Thank you for choosing our service!</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            SendEmailRequest request = SendEmailRequest.builder()
                    .from(formattedFrom)
                    .to(formattedTo)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            SendEmailResponse response = resend.emails().send(request);
            log.info("Appointment confirmation email sent successfully to: {}, Message ID: {}", toEmail, response.getId());
        } catch (ResendException e) {
            log.error("Failed to send appointment confirmation email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send appointment confirmation email: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            log.error("Email validation error: {}", e.getMessage());
            throw e;
        }
    }
}