package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedUserPresenter {

    public FollowersPresenter(PagedPresenter.View<User> view) {
        super(view);
    }

    @Override
    String getDescription() {
        return "followers";
    }

    @Override
    public void callTask(User user) {
        getFollowService().getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetItemsObserver());
    }
}
