package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.util.AlmostStatus;
import edu.byu.cs.tweeter.util.ResultsPage;

public interface StatusDAO {
    public ResultsPage<AlmostStatus> getStory(PagedRequest request);
    public ResultsPage<AlmostStatus> getFeed(PagedRequest request);
    public void addStatusToFeed(String user, Status status);
    public void addStatusToStory(String user, Status status);
}
