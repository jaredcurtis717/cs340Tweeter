package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that returns statuses for a user's story
 */
public class GetFeedHandler implements RequestHandler<PagedRequest, StatusesResponse> {

    /**
     * Returns the statuses of the user's feed.
     * Implements paged responses, returning a given number of items specified in request
     * @param input contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public StatusesResponse handleRequest(PagedRequest input, Context context) {
        StatusService statusService = new StatusService();
        return statusService.getFeed(input);
    }
}