package com.prescripto.springBackend.filter;

import com.prescripto.springBackend.util.EnvUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.ServletException;

import java.io.IOException;

public class AuthUserFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Not Authorized Login Again\"}");
            return;
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(EnvUtil.getJwt_Secret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            // Attach userId to request so controller can use it
            request.setAttribute("userId", Long.parseLong(claims.getSubject()));

            filterChain.doFilter(request, response); // proceed to controller

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

