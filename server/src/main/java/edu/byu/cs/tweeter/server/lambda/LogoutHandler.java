package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.AuthTokenRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.UserService;

public class LogoutHandler implements RequestHandler<AuthTokenRequest, Response> {
    /**
     * Returns the statuses of the user's feed.
     * Implements paged responses, returning a given number of items specified in request
     * @param input contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public Response handleRequest(AuthTokenRequest input, Context context) {
        UserService userService = new UserService();
        return userService.logout(input);
    }
}
