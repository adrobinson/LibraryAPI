package com.example.Library.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    // Runs for every incoming HTTP request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

    if(authHeader != null && authHeader.startsWith("Bearer ")) { /// Check if auth header exists & contains a jwt
        String jwt = authHeader.substring(7); // Extract token from the header
        String username = jwtUtil.extractClaims(jwt).getSubject(); // Extract username from the token

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){ // Checks a username was extracted and that nobody is already authenticated in this request (prevent overwriting)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // loads user into a Spring Security UserDetails.User object

            if(jwtUtil.validateToken(jwt, userDetails)) { // checks the token has not been tampered with and is not expired
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(token); // creates authentication object to tell who the user is and what permissions they have
            }
        }
    }

    filterChain.doFilter(request,response); // passes request along to next filter


    }
}
