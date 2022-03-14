package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.BoolResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that tells if a user is following another or not
 */
public class IsFollowingHandler implements RequestHandler<IsFollowerRequest, BoolResponse> {
    @Override
    public BoolResponse handleRequest(IsFollowerRequest input, Context context) {
        FollowService followService = new FollowService();
        return followService.isFollowing(input);
    }
}
