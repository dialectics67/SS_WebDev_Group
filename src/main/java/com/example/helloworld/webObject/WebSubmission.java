package com.example.helloworld.webObject;

import java.io.Serializable;

public class WebSubmission implements Serializable {
    private Long mainUserId;

    private Integer floorId;

    private TeamMember[] teamMemberList;

    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public int getTeamMemberLength() {
        if (teamMemberList != null)
            return teamMemberList.length;
        else return 0;
    }

    public TeamMember[] getTeamMemberList() {
        return teamMemberList;
    }

    public void setTeamMemberList(TeamMember[] teamMemberList) {
        this.teamMemberList = teamMemberList;
    }


    public Long getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(Long mainUserId) {
        this.mainUserId = mainUserId;
    }
}

