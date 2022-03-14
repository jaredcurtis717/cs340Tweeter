package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedUserPresenter extends PagedPresenter<User> {
    private final FollowService followService;

    public PagedUserPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    protected FollowService getFollowService(){
        return followService;
    }
}
