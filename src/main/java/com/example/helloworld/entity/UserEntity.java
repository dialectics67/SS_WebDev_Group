package com.example.helloworld.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "ss_web")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "user_id")
    private Long userId;
    @Basic
    @Column(name = "user_name")
    private String userName;
    @Basic
    @Column(name = "user_sex")
    private Integer userSex;
    @Basic
    @Column(name = "user_type")
    private Integer userType;
    @Basic
    @Column(name = "birthday")
    private Date birthday;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "check_code")
    private String checkCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName) && Objects.equals(userSex, that.userSex) && Objects.equals(userType, that.userType) && Objects.equals(birthday, that.birthday) && Objects.equals(password, that.password) && Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, userName, userSex, userType, birthday, password, phone);
    }


}
