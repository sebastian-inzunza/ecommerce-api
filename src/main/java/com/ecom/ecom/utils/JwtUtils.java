package com.ecom.ecom.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtils {

    public static final String SECRET= "7f78891feed042e55f1f7bfe587d3b4d9c11f68f65c04be57b112aac9ad8283b757da64fb8bd94bd5b84dfc54d26544bfdfbf4db7e440f422914648b06b9b719391fad52a1291cac4a309a9c81487b70ae77976386264e31d68cb1ad9e0bcd6441952520e0a1a4517b3fb5999926c0d261aa5b704cf66891a00baa71c18d42954b895e8d67a7b1b9747c39fffbc1a7cef2735c1dd834681cef5f7817dd27074b90b2b62a049d6925898fc5e0d065af2e76edfad738de8825ee027c53c358b56bffa49c94590922ca862e0261d8dface8711334dd0c6c51e27a91437ec5391d177f39307a6e97f4890132219a48f40f66b344804e66495bf2d75b5eee1f55e56f";

    public String generateToken(String username ){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims , String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +10000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }


    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return extraExpiration(token).before(new Date());
    }

    public Date extraExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }
    

    public Boolean validatetoken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}

