package com.example.userservice.filter;

import com.example.userservice.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

           String authHeader= request.getHeader("Authorization");

            if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
               filterChain.doFilter(request,response);
               return;
           }

           String token=authHeader.substring(7);
            Claims claims = jwtService.resolveClaims(request);
           String email =claims.getSubject();

           if(email!=null && SecurityContextHolder.getContext().getAuthentication() == null){
               UserDetails userDetails = userDetailsService.loadUserByUsername(email);

               if(userDetails != null && jwtService.isValidToken(token , userDetails)){

                   Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                   SecurityContextHolder.getContext().setAuthentication(authentication);
               }
           }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        filterChain.doFilter(request,response);

    }
}
