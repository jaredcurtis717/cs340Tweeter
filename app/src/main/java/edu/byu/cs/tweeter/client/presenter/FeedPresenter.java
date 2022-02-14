package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedStatusPresenter {
    public FeedPresenter(PagedPresenter.View<Status> view) {
        super(view);
    }

    @Override
    String getDescription() {
        return "feed";
    }

    @Override
    public void callTask(User user) {
        statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetItemsObserver());
    }
}
