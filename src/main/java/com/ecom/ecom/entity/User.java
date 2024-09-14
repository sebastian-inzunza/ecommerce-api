package com.ecom.ecom.entity;
import com.ecom.ecom.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    private UserRole role;


    @Lob
    @Column(columnDefinition= "longblob")

    private byte[] img;
    

}
