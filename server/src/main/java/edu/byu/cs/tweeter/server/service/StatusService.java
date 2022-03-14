package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.GetStatusesDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class StatusService {

    /**
     * Returns the feed of the given user. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
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
        return getStatusesDAO().getFeed(request);
    }

    /**
     * Returns the story of the given user. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. Uses the {@link FollowDAO} to
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
        return getStatusesDAO().getStory(request);
    }

    public Response postStatus(PostStatusRequest request) {
        if(request.getAuthToken() == null) {
            throw new RuntimeException("[BadRequest] invalid authToken");
        } else if(request.getStatus() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a status to post");
        }

        //Todo: this sends dummy data
        return new Response(true);
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    GetStatusesDAO getStatusesDAO() {
        return new GetStatusesDAO();
    }


}