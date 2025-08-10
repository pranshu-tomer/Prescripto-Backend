package com.prescripto.springBackend.service;

import com.prescripto.springBackend.model.User;
import com.prescripto.springBackend.repository.UserRepository;
import com.prescripto.springBackend.util.EnvUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Map<String,Object>> registerUser(User user){
        Map<String,Object> response = new HashMap<>();

        try{
            String name = user.getName();
            String email = user.getEmail();
            String password = user.getPassword();

            if(name == null || email == null || password == null || name.isEmpty() || email.isEmpty() || password.isEmpty()){
                response.put("success",false);
                response.put("message","Missing Details !!");
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
        }catch (Exception e){
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> loginUser(User user) {

        Map<String,Object> response = new HashMap<>();
        try{
            String email = user.getEmail();
            String password = user.getPassword();

            if(email == null || password == null || email.isEmpty() || password.isEmpty()){
                response.put("success",false);
                response.put("message","Missing Details !!");
                return ResponseEntity.badRequest().body(response);
            }

            User user1 = userRepository.findByEmail(email)
                    .orElse(null);

            if(user1 == null){
                response.put("success",false);
                response.put("message","User does Not Exist.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean isMatch = passwordEncoder.matches(password,user1.getPassword());
            if(!isMatch){
                response.put("success",false);
                response.put("message","Invalid credentials !!");
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
        Map<String,Object> response = new HashMap<>();

        try{
            User curr = userRepository.findById(userId).orElse(null);
            if(curr == null) {
                response.put("success",false);
                response.put("message","Can't Find User !!");
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
}
