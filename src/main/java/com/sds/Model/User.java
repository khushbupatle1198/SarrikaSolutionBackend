package com.sds.Model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private Date dob;
    private String birthTime;
    private String birthPlace;
    private String profileImage;
    private String role = "USER";
    @Column(nullable = false)
    private boolean temporary = false;
    
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<CoursePurchase> purchases;
 // Add these new fields
    private String otp;
    private Date otpExpiry;
    // Getters and Setters
    
    
    public String getOtp() {
		return otp;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getOtpExpiry() {
		return otpExpiry;
	}

	public void setOtpExpiry(Date otpExpiry) {
		this.otpExpiry = otpExpiry;
	}

	public Long getId() {
        return id;
    }

   

	public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(String birthTime) {
        this.birthTime = birthTime;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public List<CoursePurchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<CoursePurchase> purchases) {
        this.purchases = purchases;
    }

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
    
}