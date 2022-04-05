package edu.byu.cs.tweeter.server.service;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoStatusDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAO;
import edu.byu.cs.tweeter.util.ResultsPage;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class StatusService {

    /**
     * Returns the feed of the given user. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link DynamoFollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public StatusesResponse getFeed(PagedRequest request) {
        if(request.getAlias() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request needs to have a positive limit");
        }
        System.out.println("About to call validate");
        getAuthtokenDAO().validate(request.getAuthToken());

        System.out.println("About to call getFeed");
        ResultsPage resultsPage = getStatusesDAO().getFeed(request);
        List<Status> feedStatuses = new ArrayList<>();

        for(String sta : resultsPage.getValues()){
            Status status = extractStatus(sta);
            feedStatuses.add(status);
        }

        return new StatusesResponse(feedStatuses, resultsPage.hasLastKey());
    }


    /**
     * Returns the story of the given user. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. Uses the {@link DynamoFollowDAO} to
     * get the followers.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    public StatusesResponse getStory(PagedRequest request) {
        if(request.getAlias() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request needs to have a positive limit");
        }
        getAuthtokenDAO().validate(request.getAuthToken());

        ResultsPage resultsPage = getStatusesDAO().getStory(request);
        List<Status> storyStatuses = new ArrayList<>();

        for(String sta : resultsPage.getValues()){
            Status status = extractStatus(sta);
            storyStatuses.add(status);
        }

        return new StatusesResponse(storyStatuses, resultsPage.hasLastKey());
    }

    public Response postStatus(PostStatusRequest request) {
        if(request.getAuthToken() == null) {
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getStatus() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a status to post");
        }

        String currentUser = getAuthtokenDAO().validate(request.getAuthToken());

        getStatusesDAO().addStatusToStory(currentUser, request.getStatus());

        List<String> followers = getFollowDAO()
                .getFollowers(new PagedRequest(request.getAuthToken(), currentUser, 1000000, null))
                .getValues();

        for(String follower : followers){
            getStatusesDAO().addStatusToFeed(follower, request.getStatus());
        }
        return new Response(true);
    }

    /**
     * Returns an instance of {@link DynamoFollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StatusDAO getStatusesDAO() {
        return new DynamoStatusDAO();
    }

    AuthtokenDAO getAuthtokenDAO(){return new DynamoAuthtokenDAO();}

    FollowDAO getFollowDAO(){return new DynamoFollowDAO();}
}