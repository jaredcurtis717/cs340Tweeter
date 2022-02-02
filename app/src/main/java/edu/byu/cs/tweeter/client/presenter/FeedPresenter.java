package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {
    private static final int PAGE_SIZE = 10;
    private boolean isLoading;
    private boolean hasMorePages;
    private Status lastStatus;
    private final View view;
    private final StatusService statusService;
    private final UserService userService;
    public FeedPresenter(View view) {
        this.view = view;
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
        view.addLoadingFooter();
        statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedObserver());
    }

    public void getUser(String user) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), user, new GetUserObserver());
    }

    public interface View {
        void gotFeed(List<Status> statuses);

        void removeLoadingFooterInView();

        void displayErrorMessage(String message);

        void addLoadingFooter();

        void gotUser(User user);
    }

    private class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.gotUser(user);
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

    private class GetFeedObserver implements StatusService.GetFeedObserver {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.gotFeed(statuses);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get feed because of exception: " + exception.getMessage());
        }

        @Override
        public void setLoading(boolean value) {
            isLoading = value;
        }

        @Override
        public void removeLoadingFooter() {
            view.removeLoadingFooterInView();
        }
    }

}
