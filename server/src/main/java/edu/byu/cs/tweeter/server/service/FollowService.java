package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.BoolResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.util.DataAccessException;
import edu.byu.cs.tweeter.util.ResultsPage;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    private final DAOFactory daoFactory;

    public FollowService(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link DynamoFollowDAO} to
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
        getAuthtokenDAO().validate(request.getAuthToken());

        ResultsPage resultsPage = getFollowDAO().getFollowees(request);
        List<User> followeeUsers = new ArrayList<>();

        UserDAO userDAO = getUserDAO();

        for(String followeeHandle : resultsPage.getValues()){
            try{
                followeeUsers.add(userDAO.getUser(followeeHandle));
            } catch (DataAccessException e) {
                return new FollowingResponse(e.getMessage());
            }
        }

        return new FollowingResponse(followeeUsers, resultsPage.hasLastKey());
    }

    /**
     * Returns the users that follow the user. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. Uses the {@link DynamoFollowDAO} to
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
        getAuthtokenDAO().validate(request.getAuthToken());

        ResultsPage resultsPage = getFollowDAO().getFollowers(request);
        List<User> followerUsers = new ArrayList<>();

        UserDAO userDAO = getUserDAO();

        for(String followerHandle : resultsPage.getValues()){
            try{
                followerUsers.add(userDAO.getUser(followerHandle));
            } catch (DataAccessException e) {
                return new FollowingResponse(e.getMessage());
            }
        }

        return new FollowingResponse(followerUsers, resultsPage.hasLastKey());
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
     * Returns an instance of {@link DynamoFollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowDAO() {
        return daoFactory.getFollowDao();
    }

    UserDAO getUserDAO(){
        return daoFactory.getUserDAO();
    }

    AuthtokenDAO getAuthtokenDAO(){return daoFactory.getAuthtokenDAO();}

}
