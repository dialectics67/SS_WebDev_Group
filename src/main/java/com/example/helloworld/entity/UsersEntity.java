package com.example.helloworld.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "dormitory")
public class UsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "uid", nullable = false)
    private Integer uid;
    @Basic
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Basic
    @Column(name = "gender", nullable = false)
    private Integer gender;
    @Basic
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Basic
    @Column(name = "tel", nullable = false, length = 100)
    private String tel;
    @Basic
    @Column(name = "type", nullable = false)
    private Integer type;
    @Basic
    @Column(name = "add_time", nullable = false)
    private Integer addTime;
    @Basic
    @Column(name = "is_deny", nullable = false)
    private Integer isDeny;
    @Basic
    @Column(name = "last_login_time", nullable = false)
    private Integer lastLoginTime;
    @Basic
    @Column(name = "remarks", nullable = true, length = 1000)
    private String remarks;
    @Basic
    @Column(name = "is_del", nullable = false)
    private Integer isDel;
    @Basic
    @Column(name = "status", nullable = false)
    private Integer status;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public Integer getIsDeny() {
        return isDeny;
    }

    public void setIsDeny(Integer isDeny) {
        this.isDeny = isDeny;
    }

    public Integer getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Integer lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return Objects.equals(uid, that.uid) && Objects.equals(name, that.name) && Objects.equals(gender, that.gender) && Objects.equals(email, that.email) && Objects.equals(tel, that.tel) && Objects.equals(type, that.type) && Objects.equals(addTime, that.addTime) && Objects.equals(isDeny, that.isDeny) && Objects.equals(lastLoginTime, that.lastLoginTime) && Objects.equals(remarks, that.remarks) && Objects.equals(isDel, that.isDel) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, gender, email, tel, type, addTime, isDeny, lastLoginTime, remarks, isDel, status);
    }
}
