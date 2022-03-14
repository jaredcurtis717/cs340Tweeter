package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedUserPresenter {

    public FollowingPresenter(PagedPresenter.View<Status> view) {
        super(view);
    }

    @Override
    String getDescription() {
        return "following";
    }

    @Override
    public void callTask(User user) {
        getFollowService().getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetItemsObserver());
    }
}
