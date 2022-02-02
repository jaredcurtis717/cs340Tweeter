package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    public interface View{
        void followSuccessful();
        void setFollowButtonEnabled(boolean value);
        void displayErrorMessage(String message);

        void unFollowSuccessful();
    }

    private View view;
    FollowService followService;

    public MainPresenter(View view){
        this.view = view;
        followService = new FollowService();
    }

    public void unfollow(User user) {
        followService.unFollow(Cache.getInstance().getCurrUserAuthToken(), user, new UnFollowObserver());
    }

    public void follow(User user){
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), user, new followObserver());
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
}
