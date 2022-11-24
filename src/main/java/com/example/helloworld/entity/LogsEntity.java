package com.example.helloworld.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "logs", schema = "dormitory")
public class LogsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "uid", nullable = true)
    private Integer uid;
    @Basic
    @Column(name = "operation", nullable = false, length = 500)
    private String operation;
    @Basic
    @Column(name = "client_ip", nullable = false, length = 50)
    private String clientIp;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Integer createTime;
    @Basic
    @Column(name = "content", nullable = false, length = 1000)
    private String content;
    @Basic
    @Column(name = "status", nullable = false)
    private Integer status;
    @Basic
    @Column(name = "is_del", nullable = false)
    private Integer isDel;

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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogsEntity that = (LogsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(uid, that.uid) && Objects.equals(operation, that.operation) && Objects.equals(clientIp, that.clientIp) && Objects.equals(createTime, that.createTime) && Objects.equals(content, that.content) && Objects.equals(status, that.status) && Objects.equals(isDel, that.isDel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, operation, clientIp, createTime, content, status, isDel);
    }
}
