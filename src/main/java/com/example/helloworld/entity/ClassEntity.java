package com.example.helloworld.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "class", schema = "dormitory")
public class ClassEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = false, length = 20)
    private String name;
    @Basic
    @Column(name = "remarks", nullable = false, length = 1000)
    private String remarks;
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
        ClassEntity that = (ClassEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(remarks, that.remarks) && Objects.equals(isDel, that.isDel) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, remarks, isDel, status);
    }
}
