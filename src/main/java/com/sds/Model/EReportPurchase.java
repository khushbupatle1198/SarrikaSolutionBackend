package com.sds.Model;

import jakarta.persistence.*;
import java.util.Date;




@Entity

@Table(name = "ereport_purchases")
public class EReportPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "ereport_id")
    private EReport eReport;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(name = "date_of_birth", nullable = false)
    private String dob;
    
    @Column(name = "birth_time")
    private String birthTime;
    
    @Column(name = "birth_place")
    private String birthPlace;
    
    @Column(name = "specific_questions", columnDefinition = "TEXT")
    private String specificQuestions;
    
    @Column(name = "payment_proof_url", nullable = false)
    private String paymentProofUrl;
    
    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    
    @Column(name = "purchase_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDate = new Date();
    
    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EReport getEReport() { return eReport; }
    public void setEReport(EReport eReport) { this.eReport = eReport; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getBirthTime() { return birthTime; }
    public void setBirthTime(String birthTime) { this.birthTime = birthTime; }
    public String getBirthPlace() { return birthPlace; }
    public void setBirthPlace(String birthPlace) { this.birthPlace = birthPlace; }
    public String getSpecificQuestions() { return specificQuestions; }
    public void setSpecificQuestions(String specificQuestions) { this.specificQuestions = specificQuestions; }
    public String getPaymentProofUrl() { return paymentProofUrl; }
    public void setPaymentProofUrl(String paymentProofUrl) { this.paymentProofUrl = paymentProofUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }
}