package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that posts a status
 */
public class PostStatusHandler implements RequestHandler<PostStatusRequest, Response> {

    @Override
    public Response handleRequest(PostStatusRequest input, Context context) {
        StatusService statusService = new StatusService();
        return statusService.postStatus(input);
    }
}
