package edu.byu.cs.tweeter.server.model;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostQDTO {
    String userAlias;
    Status status;

    public PostQDTO(String userAlias, Status status) {
        this.userAlias = userAlias;
        this.status = status;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
