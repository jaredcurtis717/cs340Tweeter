package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.AuthTokenRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.BoolResponse;
import edu.byu.cs.tweeter.model.net.response.IntResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;


/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    private static final String SERVER_URL = "https://r63ysncqqe.execute-api.us-west-2.amazonaws.com/dev";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    /**
     * registers the user and if successful, returns the new user and an authToken
     * @param request contains all information needed to register a user
     * @param urlPath path to the register endpoint
     * @return the now logged in user and authToken, or failure and message
     */
    public LoginResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(PagedRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
    }

    /**
     * Returns the statuses that the user specified in the request is following. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains information about the user whose statuses are to be returned and any
     *                other information required to satisfy the request.
     * @return the statuses.
     */
    public StatusesResponse getStatuses(PagedRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, StatusesResponse.class);
    }

    /**
     * sets the current user to be following the user designated in request.
     * @param request contains the current logged in users authToken and the alias to be followed
     * @param urlPath path to follow endpoint
     * @return basic response
     */
    public Response follow(TargetUserRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, Response.class);
    }

    /**
     * Tells whether a user follows another user or not
     * @param request contains current user's authToken and which user to check
     * @param urlPath path to isFollowing endpoint
     * @return whether is a follower or not
     */
    public BoolResponse isFollower(IsFollowerRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, BoolResponse.class);
    }

    /**
     * Posts a status
     * @param request contains the post's and current user's information
     * @param urlPath path to the poststatus enpoint
     * @return basic response
     */
    public Response postStatus(PostStatusRequest request, String urlPath) throws Exception{
        return clientCommunicator.doPost(urlPath, request, null, Response.class);
    }

    /**
     * gets the count of how many users follow someone
     * @param request contains which user is being queried
     * @param urlPath path to the getfollowerscount endpoint
     * @return number of followers
     */
    public IntResponse getFollowersCount(TargetUserRequest request, String urlPath) throws Exception{
        return clientCommunicator.doPost(urlPath, request, null, IntResponse.class);
    }

    /**
     * gets the count of how many users follow someone
     * @param request contains which user is being queried
     * @param urlPath path to the getfolloweescount endpoint
     * @return number of followees
     */
    public IntResponse getFolloweesCount(TargetUserRequest request, String urlPath) throws Exception{
        return clientCommunicator.doPost(urlPath, request, null, IntResponse.class);
    }

    /**
     * logs the user out
     * @param request contains authtoken of logged in user
     * @param urlPath path to logout endpoint
     * @return basic succeed/fail response
     */
    public Response logout(AuthTokenRequest request, String urlPath) throws Exception {
        return clientCommunicator.doPost(urlPath, request, null, Response.class);
    }

    /**
     * gets a chosen user
     * @param request contains which user is being sought after
     * @param urlPath path to the getuser enpoint
     * @return the user
     */
    public UserResponse getUser(TargetUserRequest request, String urlPath) throws Exception{
        return clientCommunicator.doPost(urlPath,request,null,UserResponse.class);
    }
}
