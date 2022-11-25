package com.example.helloworld.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "buildings", schema = "dormitory")
public class BuildingsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Basic
    @Column(name = "order_num", nullable = false)
    private Integer orderNum;
    @Basic
    @Column(name = "is_valid", nullable = false)
    private Integer isValid;
    @Basic
    @Column(name = "remarks", nullable = true, length = 1000)
    private String remarks;
    @Basic
    @Column(name = "describe", nullable = false, length = 1000)
    private String describe;
    @Basic
    @Column(name = "image_url", nullable = false, length = 100)
    private String imageUrl;
    @Basic
    @Column(name = "is_del", nullable = false)
    private Integer isDel;
    @Basic
    @Column(name = "status", nullable = false)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        BuildingsEntity that = (BuildingsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(orderNum, that.orderNum) && Objects.equals(isValid, that.isValid) && Objects.equals(remarks, that.remarks) && Objects.equals(describe, that.describe) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(isDel, that.isDel) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, orderNum, isValid, remarks, describe, imageUrl, isDel, status);
    }
}
