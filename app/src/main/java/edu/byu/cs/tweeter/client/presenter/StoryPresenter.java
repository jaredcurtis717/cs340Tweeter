package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {
    private static final int PAGE_SIZE = 10;

    public interface View{
        void displayNewStatuses(List<Status> statuses);
        void clickedUser(User user);
        void displayErrorMessage(String s);
        void addLoadingFooter();
        void removeLoadingFooter();
        void displayGettingUser();
    }

    UserService userService;
    StoryService storyService;
    View view;
    Status lastStatus;

    private boolean isLoading;
    private boolean hasMorePages;

    public StoryPresenter(View view){
        this.view = view;
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
            view.addLoadingFooter();

            storyService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetStoryObserver());
        }
    }
    public void setLastStatus(Status status){
        lastStatus = status;
    }
    public boolean getHasMorePages(){
        return hasMorePages;
    }
    public void setHasMorePages(boolean value){
        hasMorePages = value;
    }
    public boolean getIsLoading(){
        return isLoading;
    }

    public class GetStoryObserver implements StoryService.GetStoryObserver{

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages, Status lastStatus) {
            setHasMorePages(hasMorePages);
            setLastStatus(lastStatus);
            view.displayNewStatuses(statuses);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get story: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get story because of exception: " + exception.getMessage());
        }

        @Override
        public void removeLoadingFooter() {
            view.removeLoadingFooter();
        }

        @Override
        public void setLoading(boolean value) {
            isLoading = value;
        }
    }

    public class GetUserObserver implements UserService.GetUserObserver{

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
