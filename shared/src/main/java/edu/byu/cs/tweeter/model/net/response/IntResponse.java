package edu.byu.cs.tweeter.model.net.response;

public class IntResponse extends Response{
    private int number;

    /**
     * Creates a response that contains an integer value
     *
     * @param number the number of followers/followees
     */
    public IntResponse(int number) {
        super(true);
        this.number = number;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public IntResponse(String message) {
        super(false, message);
    }

    /**
     * returns an integer for how many followers/followees a user has
     * @return the integer
     */
    public int getNumber(){
        return number;
    }
}
