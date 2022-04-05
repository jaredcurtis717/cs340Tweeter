package edu.byu.cs.tweeter.server.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.xspec.L;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthTokenRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.IntResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.util.DataAccessException;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.ResultsPage;

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

        User user;
        try{
            user = getUserDAO().login(request);
        } catch (Exception e) {
            return new LoginResponse(e.getMessage());
        }

        AuthToken authToken = getAuthtokenDAO().newAuthtoken(request.getUsername());

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
        try{

            getAuthtokenDAO().removeToken(request);
        } catch (DataAccessException e) {
            throw new RuntimeException("[BadRequest] Unable to delete authtoken");
        }

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

        User newUser;
        AuthToken authtoken;
        User userShouldNotExist = null;
        try{
            //expecting an exception letting us know the user doesn't exist
             userShouldNotExist = getUserDAO().getUser(request.getUsername());
        }catch (Exception ignored){
        }

        if (userShouldNotExist != null){
            throw new RuntimeException("[BadRequest] user already exists");
        }
        System.out.println("User doesn't exist, creating new user");

        try{
            newUser = getUserDAO().registerUser(request);
            authtoken = getAuthtokenDAO().newAuthtoken(request.getUsername());
        }catch (Exception ex){
            return new LoginResponse(ex.getMessage());
        }

        return new LoginResponse(newUser, authtoken);

        /*
        // old fake data implementation
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new LoginResponse(user, authToken);
         */
    }

    public IntResponse getFollowerCount(TargetUserRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getUser() == null){
            throw new RuntimeException("[BadRequest] missing user");
        }
        PagedRequest pagedRequest = new PagedRequest(request.getAuthToken(),request.getUser(), 1000000, null);

        ResultsPage resultsPage = getFollowDAO().getFollowers(pagedRequest);

        return new IntResponse(resultsPage.getValues().size());
    }

    public IntResponse getFollowingCount(TargetUserRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getUser() == null){
            throw new RuntimeException("[BadRequest] missing user");
        }

        PagedRequest pagedRequest = new PagedRequest(request.getAuthToken(),request.getUser(), 1000000, null);

        ResultsPage resultsPage = getFollowDAO().getFollowees(pagedRequest);

        return new IntResponse(resultsPage.getValues().size());
    }

    public UserResponse getUser(TargetUserRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getUser() == null){
            throw new RuntimeException("[BadRequest] missing user");
        }

        getAuthtokenDAO().validate(request.getAuthToken());

        User answerUser;
        try{
            answerUser = getUserDAO().getUser(request.getUser());
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] failed to get user");
        }

        return new UserResponse(answerUser);
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

    FollowDAO getFollowDAO(){return daoFactory.getFollowDao();}

    AuthtokenDAO getAuthtokenDAO(){
        return daoFactory.getAuthtokenDAO();
    }

}
