package com.ecom.ecom.entity;
import com.ecom.ecom.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name ="users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;


    private String email;

    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // Esto es opcional, pero puede ayudar a evitar valores nulos en la columna
    private UserRole role;


    @Lob
    @Column(columnDefinition= "longblob")

    private byte[] img;
    

}
