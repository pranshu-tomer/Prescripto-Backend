package com.prescripto.springBackend.dto;

import com.prescripto.springBackend.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.web.multipart.MultipartFile;

public class ProfileUpdateDTO {

    public enum Gender {
        Male,
        Female,
    }

    private String name;
    private String dob;
    private String phone;
    @Enumerated(EnumType.STRING)
    private User.Gender gender;
    private MultipartFile image; // file field name must match frontend form

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
        this.gender = gender;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}

