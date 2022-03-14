package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.BoolResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(PagedRequest request) {
        if(request.getAlias() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request needs to have a positive limit");
        }
        return getFollowingDAO().getFollowees(request);
    }

    /**
     * Returns the users that follow the user. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followers.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    public FollowingResponse getFollowers(PagedRequest request) {
        if(request.getAlias() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request needs to have a positive limit");
        }
        return getFollowingDAO().getFollowers(request);
    }

    public Response follow(TargetUserRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have user to follow");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        return new Response(true, null);
    }

    public Response unfollow(TargetUserRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have user to unfollow");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        return new Response(true, null);
    }

    public BoolResponse isFollowing(IsFollowerRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getFollowee() == null) {
            throw new RuntimeException("[BadRequest] Request a followee");
        } else if(request.getFollower() == null) {
            throw new RuntimeException("[BadRequest] Request a follower");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        return new BoolResponse(true);
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }



}
