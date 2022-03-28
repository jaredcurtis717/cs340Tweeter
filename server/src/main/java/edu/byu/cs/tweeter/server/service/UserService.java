package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthTokenRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.IntResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {

    private final DAOFactory daoFactory;

    public UserService(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public LoginResponse login(LoginRequest request) {
        System.out.println("Received request: " + request.toString());

        if(request.getUsername() == null){
            throw new RuntimeException("[BadRequest] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[BadRequest] Missing a password");
        }
        //TODO checkPassword
        User user = getUserDAO().getUser(request.getUsername());

        //TODO replace dummy authToken
        AuthToken authToken = getDummyAuthToken();

        System.out.println("User is: " + user.toString());

        if(user == null){
            throw new RuntimeException("[BadRequest] invalid username/password");
        }

        return new LoginResponse(user, authToken);
        /*
        // old dummy implementation
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new LoginResponse(user, authToken);
         */
    }

    public Response logout(AuthTokenRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        }
        // TODO: Generates dummy data. Replace with a real implementation.
        return new Response(true);
    }

    public LoginResponse register(RegisterRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[BadRequest] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[BadRequest] Missing a password");
        } else if(request.getFirstName() == null) {
            throw new RuntimeException("[BadRequest] Missing a first name");
        } else if(request.getLastName() == null) {
            throw new RuntimeException("[BadRequest] Missing a last name");
        } else if(request.getImage() == null) {
            throw new RuntimeException("[BadRequest] Missing an image");
        }


        // TODO: Generates dummy data. Replace with a real implementation.
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new LoginResponse(user, authToken);
    }

    public IntResponse getFollowerCount(TargetUserRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getUser() == null){
            throw new RuntimeException("[BadRequest] missing user");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        return new IntResponse(20);
    }

    public IntResponse getFollowingCount(TargetUserRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getUser() == null){
            throw new RuntimeException("[BadRequest] missing user");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        return new IntResponse(20);
    }

    public UserResponse getUser(TargetUserRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getUser() == null){
            throw new RuntimeException("[BadRequest] missing user");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        return new UserResponse(getFakeData().findUserByAlias(request.getUser()));
    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }

    UserDAO getUserDAO(){
        return daoFactory.getUserDAO();
    }

}
