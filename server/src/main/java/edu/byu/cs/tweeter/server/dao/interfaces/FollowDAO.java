package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.ResultsPage;

public interface FollowDAO {
    public ResultsPage<String> getFollowees(PagedRequest request);
    public ResultsPage<String> getFollowers(String alias, String lastItem, int limit);
    public boolean follow(String currentUser, String targetAlias);
    public boolean unfollow(String currentUser, String targetAlias);
    public boolean isFollowing(String user, String targetUser);

    void addFollowersBatch(List<String> followers, String followTarget);
}
