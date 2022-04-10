package edu.byu.cs.tweeter.server.model;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class JobQDTO {
    Status status;
    List<String> followers;

    public JobQDTO(Status status, List<String> followers) {
        this.status = status;
        this.followers = followers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }
}
