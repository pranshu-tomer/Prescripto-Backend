package com.prescripto.springBackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.prescripto.springBackend.dto.ProfileUpdateDTO;
import com.prescripto.springBackend.model.User;
import com.prescripto.springBackend.repository.UserRepository;
import com.prescripto.springBackend.util.EnvUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    public ResponseEntity<Map<String, Object>> registerUser(User user) {
        Map<String, Object> response = new HashMap<>();

        try {
            String name = user.getName();
            String email = user.getEmail();
            String password = user.getPassword();

            if (name == null || email == null || password == null || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                response.put("success", false);
                response.put("message", "Missing Details !!");
                return ResponseEntity.badRequest().body(response);
            }

            if (password.length() < 8) {
                response.put("success", false);
                response.put("message", "Please enter a strong password");
                return ResponseEntity.badRequest().body(response);
            }

            String hashedPassword = passwordEncoder.encode(password);
            user.setPassword(hashedPassword);

            User savedUser = userRepository.save(user);
            String token = Jwts.builder()
                    .setSubject(savedUser.getId().toString())
                    .signWith(SignatureAlgorithm.HS256, EnvUtil.getJwt_Secret())
                    .compact();

            response.put("success", true);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> loginUser(User user) {

        Map<String, Object> response = new HashMap<>();
        try {
            String email = user.getEmail();
            String password = user.getPassword();

            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                response.put("success", false);
                response.put("message", "Missing Details !!");
                return ResponseEntity.badRequest().body(response);
            }

            User user1 = userRepository.findByEmail(email)
                    .orElse(null);

            if (user1 == null) {
                response.put("success", false);
                response.put("message", "User does Not Exist.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean isMatch = passwordEncoder.matches(password, user1.getPassword());
            if (!isMatch) {
                response.put("success", false);
                response.put("message", "Invalid credentials !!");
                return ResponseEntity.badRequest().body(response);
            }

            String token = Jwts.builder()
                    .setSubject(String.valueOf(user1.getId()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                    .signWith(SignatureAlgorithm.HS256, EnvUtil.getJwt_Secret().getBytes())
                    .compact();

            response.put("success", true);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getProfile(Long userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            User curr = userRepository.findById(userId).orElse(null);
            if (curr == null) {
                response.put("success", false);
                response.put("message", "Can't Find User !!");
                return ResponseEntity.badRequest().body(response);
            }
            curr.setPassword(null);

            response.put("success", true);
            response.put("userData", curr);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> updateProfile(Long userId, ProfileUpdateDTO profileData) throws IOException {
        Map<String,Object> response = new HashMap<>();
        try {

            MultipartFile image = profileData.getImage();
            String name = profileData.getName();
            String dob = profileData.getDob();
            String phone = profileData.getPhone();
            User.Gender gender = profileData.getGender();

            String imageUrl = null;
            if(image != null && !image.isEmpty()){
                imageUrl = cloudinary.uploader()
                        .upload(image.getBytes(), ObjectUtils.asMap("folder", "profiles"))
                        .get("secure_url")
                        .toString();
            }

            if(name == null || name.isEmpty()){
                response.put("success", false);
                response.put("message", "Missing Data");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userRepository.findById(userId).orElse(null);
            if(user == null){
                response.put("success", false);
                response.put("message", "Can't Find User !!");
                return ResponseEntity.badRequest().body(response);
            }

            user.setName(name);
            user.setDob(dob);
            user.setGender(gender);
            user.setImageUrl(imageUrl);

            if(phone != null && !phone.isEmpty()){
                user.setPhone(phone);
            }

            userRepository.save(user);

            response.put("success", true);
            response.put("message","Profile Updated");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

