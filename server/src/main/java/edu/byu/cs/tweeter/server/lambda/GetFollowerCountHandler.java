package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.IntResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetFollowerCountHandler implements RequestHandler<TargetUserRequest, IntResponse> {

    @Override
    public IntResponse handleRequest(TargetUserRequest input, Context context) {
        UserService userService = new UserService();
        return userService.getFollowerCount(input);
    }
}
