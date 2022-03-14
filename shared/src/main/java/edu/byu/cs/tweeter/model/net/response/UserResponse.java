package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class UserResponse extends Response{
    private User user;

    /**
     * Creates a response that contains a user
     *
     * @param user the user
     */
    public UserResponse(User user) {
        super(true);
        this.user = user;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public UserResponse(String message) {
        super(false, message);
    }

    /**
     * returns the user that is being asked for
     * @return the user
     */
    public User getUser(){return user;}
}
