package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

public interface FollowDAO {
    public Integer getFolloweeCount(User follower);
    public FollowingResponse getFollowees(PagedRequest request);
    public FollowingResponse getFollowers(PagedRequest request);
}
