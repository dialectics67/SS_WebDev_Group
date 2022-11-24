package com.example.helloworld.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "orders", schema = "dormitory")
public class OrdersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "uid", nullable = true)
    private Integer uid;
    @Basic
    @Column(name = "group_id", nullable = false)
    private Integer groupId;
    @Basic
    @Column(name = "building_id", nullable = false)
    private Integer buildingId;
    @Basic
    @Column(name = "submit_time", nullable = false)
    private Integer submitTime;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Integer createTime;
    @Basic
    @Column(name = "finish_time", nullable = false)
    private Integer finishTime;
    @Basic
    @Column(name = "room_id", nullable = false)
    private Integer roomId;
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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Integer submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
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
        OrdersEntity that = (OrdersEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(uid, that.uid) && Objects.equals(groupId, that.groupId) && Objects.equals(buildingId, that.buildingId) && Objects.equals(submitTime, that.submitTime) && Objects.equals(createTime, that.createTime) && Objects.equals(finishTime, that.finishTime) && Objects.equals(roomId, that.roomId) && Objects.equals(remarks, that.remarks) && Objects.equals(isDel, that.isDel) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, groupId, buildingId, submitTime, createTime, finishTime, roomId, remarks, isDel, status);
    }
}
