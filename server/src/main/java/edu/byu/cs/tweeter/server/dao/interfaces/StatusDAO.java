package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;

public interface StatusDAO {
    public StatusesResponse getStory(PagedRequest request);
    public StatusesResponse getFeed(PagedRequest request);
}
