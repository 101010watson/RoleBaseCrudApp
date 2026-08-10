package com.example.crudapp.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private String plainRole;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String plainRole){
        super(username,password,authorities);
        this.plainRole = plainRole;
    }
    public String getPlainRole(){
        return plainRole;
    }
}

// this class is created for displaying clean Role in the page
// if not created the the sec:authentication="principal.authority" gives Role_ as the prefix which does not look good