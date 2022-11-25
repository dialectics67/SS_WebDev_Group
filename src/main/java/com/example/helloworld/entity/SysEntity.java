package com.example.helloworld.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sys", schema = "dormitory")
public class SysEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "key_name", nullable = false, length = 100)
    private String keyName;
    @Basic
    @Column(name = "key_value", nullable = false, length = 1000)
    private String keyValue;
    @Basic
    @Column(name = "is_del", nullable = false)
    private Integer isDel;
    @Basic
    @Column(name = "remarks", nullable = true, length = 1000)
    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysEntity sysEntity = (SysEntity) o;
        return Objects.equals(id, sysEntity.id) && Objects.equals(keyName, sysEntity.keyName) && Objects.equals(keyValue, sysEntity.keyValue) && Objects.equals(isDel, sysEntity.isDel) && Objects.equals(remarks, sysEntity.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keyName, keyValue, isDel, remarks);
    }
}
