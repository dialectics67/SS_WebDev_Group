package com.example.helloworld.webObject;

import java.io.Serializable;

public class TeamMember implements Serializable {
    private Long userId;
    private String checkCode;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}
