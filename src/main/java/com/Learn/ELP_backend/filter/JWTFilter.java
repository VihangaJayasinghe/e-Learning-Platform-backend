package com.Learn.ELP_backend.filter;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Learn.ELP_backend.service.JWTService;
import com.Learn.ELP_backend.service.userDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtservice;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        String AuthHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        String role = null;

        if (AuthHeader != null && AuthHeader.startsWith("Bearer ")) {
            token = AuthHeader.substring(7);
            username = jwtservice.ExtractUsername(token);
            role = jwtservice.ExtractRole(token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(userDetailsService.class).loadUserByUsername(username);

            if (jwtservice.ValidateToken(token,userDetails)) {
                UsernamePasswordAuthenticationToken Authtoken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                Authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(Authtoken);
            }
            
        }

        filterChain.doFilter(request, response);

    }

}
