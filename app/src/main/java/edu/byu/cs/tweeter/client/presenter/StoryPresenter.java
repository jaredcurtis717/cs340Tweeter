package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetUserNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter{
    private static final int PAGE_SIZE = 10;
    UserService userService;
    StoryService storyService;
    Status lastStatus;
    private boolean isLoading;
    private boolean hasMorePages;

    public StoryPresenter(PagedPresenter.View<Status> view) {
        super(view);
        userService = new UserService();
        storyService = new StoryService();
        isLoading = false;
    }

    public void clickUser(AuthToken authToken, String userAlias) {
        userService.getUser(authToken, userAlias, new GetUserObserver());
    }

    public void loadItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);

            storyService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetStoryObserver());

        }
    }

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean value) {
        hasMorePages = value;
    }

    public boolean getIsLoading() {
        return isLoading;
    }


    public class GetStoryObserver implements PagedNotificationObserver<Status> {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            setHasMorePages(hasMorePages);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

            isLoading = false;
            view.setLoadingStatus(false);

            view.addItems(statuses);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get story: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get story because of exception: " + exception.getMessage());
        }
    }

    public class GetUserObserver implements GetUserNotificationObserver {

        @Override
        public void handleSuccess(User user) {
            view.clickedUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
