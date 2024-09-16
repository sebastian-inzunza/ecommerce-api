package com.ecom.ecom.services.auth;

import org.springframework.stereotype.Service;

import com.ecom.ecom.dto.SignupRequest;
import com.ecom.ecom.dto.UserDto;


@Service
public interface  AuthService {

    UserDto createUser(SignupRequest signupRequest);
    Boolean hasUserWithEmail(String email);

}