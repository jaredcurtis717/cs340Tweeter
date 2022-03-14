package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a follow/unfollow request
 */
public class FollowRequest {
    private AuthToken authToken;
    private String user;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowRequest(){}

    /**
     * Creates an instance.
     *
     * @param authToken authtoken of the current user
     * @param alias of the user to be followed/unfollowed
     */
    public FollowRequest(AuthToken authToken, String alias) {
        this.authToken = authToken;
        this.user = alias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
