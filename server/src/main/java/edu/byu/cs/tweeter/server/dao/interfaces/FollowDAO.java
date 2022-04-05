package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.ResultsPage;

public interface FollowDAO {
    public ResultsPage getFollowees(PagedRequest request);
    public ResultsPage getFollowers(PagedRequest request);
    public boolean follow(String currentUser, String targetAlias);
    public boolean unfollow(String currentUser, String targetAlias);
    public boolean isFollowing(String user, String targetUser);
}
