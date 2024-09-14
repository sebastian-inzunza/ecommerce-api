package com.ecom.ecom.filters;


import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecom.ecom.services.jwt.UserDetailsServiceImp;
import com.ecom.ecom.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter
 {
    
    private final UserDetailsServiceImp userDetailsService;

    private JwtUtils jwtUtls;


    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorazation");
        String token = null;
        String username =  null;

        if(authHeader != null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
            username = jwtUtls.extractUsername(token);
        }

        if(username != null &&  SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);


            if(jwtUtls.validatetoken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(userDetails, null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    }
}
