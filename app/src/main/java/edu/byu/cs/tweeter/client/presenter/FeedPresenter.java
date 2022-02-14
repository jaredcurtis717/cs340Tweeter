package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetUserNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter{
    private static final int PAGE_SIZE = 10;
    private boolean isLoading;
    private boolean hasMorePages;
    private Status lastStatus;
    private final StatusService statusService;
    private final UserService userService;

    public FeedPresenter(View view) {
        super(view);
        statusService = new StatusService();
        userService = new UserService();
        isLoading = false;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public void getFeed(User user) {
        isLoading = true;
        view.setLoadingStatus(true);
        statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedObserver());
    }

    public void getUser(String user) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), user, new GetUserObserver());
    }


    private class GetUserObserver implements GetUserNotificationObserver {

        @Override
        public void handleSuccess(User user) {
            view.clickedUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }

    private class GetFeedObserver implements PagedNotificationObserver<Status> {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            setLoading(false);
            view.setLoadingStatus(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(statuses);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get feed because of exception: " + exception.getMessage());
        }

        public void setLoading(boolean value) {
            isLoading = value;
        }
    }

}
