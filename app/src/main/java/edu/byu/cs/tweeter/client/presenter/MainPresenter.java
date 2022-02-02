package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    public interface View{
        void displayErrorMessage(String message);

        void followSuccessful();
        void unFollowSuccessful();

        void postSuccessful();

        void setFollowButtonEnabled(boolean value);

        void setFollowersCount(int count);
        void setFollowingCount(int count);

        void setIsFollower(boolean value);
    }

    private View view;
    FollowService followService;
    StatusService statusService;

    public MainPresenter(View view){
        this.view = view;
        followService = new FollowService();
        statusService = new StatusService();
    }

    public void unfollow(User user) {
        followService.unFollow(Cache.getInstance().getCurrUserAuthToken(), user, new UnFollowObserver());
    }

    public void follow(User user){
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), user, new followObserver());
    }

    public void postStatus(String post, String dateAndTime, List<String> urls, List<String> mentions){
        statusService.postStatus(post, Cache.getInstance().getCurrUser(), dateAndTime, urls, mentions, new PostStatusObserver());
    }

    public void getFollowersCount(User user){
        followService.getFollowersCount(Cache.getInstance().getCurrUserAuthToken(), user, new GetFollowersCountObserver());
    }
    public void getFollowingCount(User selectedUser) {
        followService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowingCountObserver());
    }

    public void isFollower(User user){
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(),Cache.getInstance().getCurrUser(), user, new IsFollowerObserver());
    }

    public class GetFollowingCountObserver implements FollowService.GetFollowingCountObserver{

        @Override
        public void handleSuccess(int count) {
            view.setFollowingCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get following count because of exception: " + exception.getMessage());
        }
    }
    
    public class GetFollowersCountObserver implements FollowService.GetFollowersCountObserver{

        @Override
        public void handleSuccess(int count) {
            view.setFollowersCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get followers count because of exception: " + exception.getMessage());
        }
    }

    public class PostStatusObserver implements StatusService.PostStatusObserver{

        @Override
        public void handleSuccess() {
            view.postSuccessful();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to post status because of exception: " + exception.getMessage());
        }
    }

    public class followObserver implements FollowService.FollowObserver{

        @Override
        public void handleSuccess() {
            view.followSuccessful();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to follow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to follow because of exception: " + exception.getMessage());
        }

        @Override
        public void setFollowButtonEnabled(boolean value) {
            view.setFollowButtonEnabled(value);
        }
    }

    public class UnFollowObserver implements FollowService.UnFollowObserver{

        @Override
        public void handleSuccess() {
            view.unFollowSuccessful();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to unfollow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to unfollow because of exception: " + exception.getMessage());
        }

        @Override
        public void setFollowButtonEnabled(boolean value) {
            view.setFollowButtonEnabled(value);
        }
    }

    public class IsFollowerObserver implements FollowService.IsFollowerObserver{

        @Override
        public void handleSuccess(boolean value) {
            view.setIsFollower(value);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }
}
