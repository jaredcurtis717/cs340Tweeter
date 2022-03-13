package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String image;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private RegisterRequest() {}

    /**
     * Creates an instance.
     *
     * @param username the username of the user to be logged in.
     * @param password the password of the user to be logged in.
     */
    public RegisterRequest(String firstName, String lastName, String username, String password, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.image = image;
    }

    /**
     * Returns the username of the user to be registered by this request.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Returns the password of the user to be registered by this request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the first name of the user to be registered by this request.
     *
     * @return the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the user to be registered by this request.
     *
     * @return the last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the image of the user to be registered by this request.
     *
     * @return the image.
     */
    public String getImage() {
        return image;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
