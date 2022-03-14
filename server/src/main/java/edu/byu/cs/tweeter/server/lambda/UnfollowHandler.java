package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.FollowService;

public class UnfollowHandler implements RequestHandler<FollowRequest, Response> {
    /**
     * Returns the statuses of the user's feed.
     * Implements paged responses, returning a given number of items specified in request
     * @param input contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public Response handleRequest(FollowRequest input, Context context) {
        FollowService followService = new FollowService();
        return followService.unfollow(input);
    }
}
