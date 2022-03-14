package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Contains all the information needed to make a postStatus request
 */
public class PostStatusRequest{
    private AuthToken authToken;
    private Status status;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code
     */
    private PostStatusRequest(){}

    /**
     * Creates an instance
     *
     * @param authToken of logged in user
     * @param status to be posted
     */
    public PostStatusRequest(AuthToken authToken, Status status){
        this.authToken = authToken;
        this.status = status;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
