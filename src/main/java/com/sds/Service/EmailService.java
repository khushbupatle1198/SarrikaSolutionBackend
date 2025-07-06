package com.sds.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sds.Model.Course;
import com.sds.Model.CoursePurchase;
import com.sds.Model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    
   final String adminEmail="sarika.shrirao@gmail.com";
    
    // Constructor injection
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send simple text email
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(adminEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    /**
     * Send email with HTML content
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) 
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(adminEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = isHtml
        
        mailSender.send(message);
    }

    /**
     * Send email with attachment
     */
    public void sendEmailWithAttachment(
            String to, 
            String subject, 
            String text,
            String attachmentPath,
            String attachmentName) throws MessagingException {
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setFrom(adminEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        
        // Add attachment
        FileSystemResource file = new FileSystemResource(attachmentPath);
        helper.addAttachment(attachmentName, file);
        
        mailSender.send(message);
    }
    
    public void sendPurchaseConfirmationEmail(User user, Course course) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setTo(user.getEmail());
        helper.setSubject("Course Purchase Confirmation: " + course.getTitle());
        
        // Simple HTML email - in production you'd use a template engine
        String htmlContent = "<html><body>" +
            "<h2>Thank you for your purchase!</h2>" +
            "<p>Dear " + user.getFullName() + ",</p>" +
            "<p>You have successfully purchased the course: <strong>" + course.getTitle() + "</strong></p>" +
            "<p>Amount: " + course.getPrice() + "</p>" +
            "<p>Your access will be activated after payment verification (usually within 24 hours).</p>" +
            "<p>Best regards,<br/>Sacred Numerology Team</p>" +
            "</body></html>";
        
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
    
    public void sendAdminPurchaseNotification(User user, Course course, CoursePurchase purchase) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        // Set admin email address (configure this properly in production)
        
        helper.setTo(adminEmail);
        helper.setSubject("New Course Purchase: " + course.getTitle());
        
        // HTML content for admin notification
        String htmlContent = "<html><body>" +
            "<h2>New Course Purchase Notification</h2>" +
            "<p><strong>Course:</strong> " + course.getTitle() + "</p>" +
            "<p><strong>Price:</strong> " + course.getPrice() + "</p>" +
            "<p><strong>Student:</strong> " + user.getFullName() + "</p>" +
            "<p><strong>Email:</strong> " + user.getEmail() + "</p>" +
            "<p><strong>Phone:</strong> " + user.getPhone() + "</p>" +
            "<p><strong>Purchase Date:</strong> " + purchase.getPurchaseDate() + "</p>" +
            "<p>Please verify the payment and activate the course access if needed.</p>" +
            "</body></html>";
        
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

   
}