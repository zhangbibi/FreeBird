package com.freebird.dto;

/**
 * Created by zhangyaping on 17/8/6.
 */
public class User {

    private Long id;
    private String username;
    private Integer password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }
}
