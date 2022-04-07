package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class UnfollowHandler implements RequestHandler<TargetUserRequest, Response> {
    /**
     * Returns the statuses of the user's feed.
     * Implements paged responses, returning a given number of items specified in request
     * @param input contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public Response handleRequest(TargetUserRequest input, Context context) {
        FollowService followService = new FollowService(new DynamoDAOFactory());
        return followService.unfollow(input);
    }
}
