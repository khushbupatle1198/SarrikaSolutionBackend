package com.sds.Model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "consultation_bookings")
public class ConsultationBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;
    
    private String name;
    private String email;
    private String phone;
    private String dob;
    private String time;
    private String placeOfBirth;
    
    @Column(length = 2000)
    private String questions;
    
    private String paymentProofUrl;
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    private Date bookingDate = new Date();
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Consultation getConsultation() { return consultation; }
    public void setConsultation(Consultation consultation) { this.consultation = consultation; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }
    public String getQuestions() { return questions; }
    public void setQuestions(String questions) { this.questions = questions; }
    public String getPaymentProofUrl() { return paymentProofUrl; }
    public void setPaymentProofUrl(String paymentProofUrl) { this.paymentProofUrl = paymentProofUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }
}