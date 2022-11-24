package com.example.helloworld.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "student_info", schema = "dormitory")
public class StudentInfoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "uid", nullable = false)
    private Integer uid;
    @Basic
    @Column(name = "studentid", nullable = false, length = 50)
    private String studentid;
    @Basic
    @Column(name = "verification_code", nullable = false, length = 10)
    private String verificationCode;
    @Basic
    @Column(name = "class_id", nullable = false)
    private Integer classId;
    @Basic
    @Column(name = "remarks", nullable = false, length = 1000)
    private String remarks;
    @Basic
    @Column(name = "status", nullable = false)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        StudentInfoEntity that = (StudentInfoEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(uid, that.uid) && Objects.equals(studentid, that.studentid) && Objects.equals(verificationCode, that.verificationCode) && Objects.equals(classId, that.classId) && Objects.equals(remarks, that.remarks) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, studentid, verificationCode, classId, remarks, status);
    }
}
