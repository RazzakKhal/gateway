package com.bookNDrive.gateway.feign.models;


import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class User extends BaseEntity {


    private String firstname;

    private String lastname;

    private String mail;

    private String password;

}

