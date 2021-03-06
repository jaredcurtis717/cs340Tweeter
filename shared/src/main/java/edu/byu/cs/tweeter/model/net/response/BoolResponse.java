package edu.byu.cs.tweeter.model.net.response;

public class BoolResponse extends Response{
    private boolean isFollowing;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param isFollowing if the follower is following the followee
     */
    public BoolResponse(boolean isFollowing) {
        super(true);
        this.isFollowing = isFollowing;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public BoolResponse(String message) {
        super(false, message);
    }

    /**
     * returns a boolean for if they are following or not
     * @return the boolean
     */
    public boolean getIsFollowing(){
        return isFollowing;
    }
}
