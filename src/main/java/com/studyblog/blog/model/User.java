package com.studyblog.blog.model;


import lombok.Data;

import java.util.Date;

@Data
public class User {
    //the unique identity char to tip the user
    private int userId;

    private String userName;

    private String hashedUserPassword;

    private Date joinDate;

    private Date birthday;

    private String gender;

    //personal signature
    private String signature;

}
