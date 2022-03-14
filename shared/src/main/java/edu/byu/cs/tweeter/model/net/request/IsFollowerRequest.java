package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make isFollowing request
 */
public class IsFollowerRequest {
    private AuthToken authToken;
    private String follower;
    private String followee;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private IsFollowerRequest(){}

    /**
     * Creates an instance
     *
     * @param authToken authtoken of logged in user
     * @param follower user alias of follower
     * @param followee user alias of followee
     */
    public IsFollowerRequest(AuthToken authToken, String follower, String followee){
        this.authToken = authToken;
        this.follower = follower;
        this.followee = followee;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowee() {
        return followee;
    }

    public void setFollowee(String followee) {
        this.followee = followee;
    }
}
