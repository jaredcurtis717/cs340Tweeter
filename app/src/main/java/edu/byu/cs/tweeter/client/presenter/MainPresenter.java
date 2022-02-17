package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.CountNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.IsFollowerNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter<MainPresenter.View>{
    public interface View extends Presenter.View{
        void followSuccessful();

        void unFollowSuccessful();

        void logoutSuccessful();

        void postSuccessful();

        void setFollowersCount(int count);

        void setFollowingCount(int count);

        void setIsFollower(boolean value);
    }

    private FollowService followService;
    private StatusService statusService;
    private LoginService loginService;

    public MainPresenter(View view) {
        super(view);
        followService = new FollowService();
        statusService = new StatusService();
        loginService = new LoginService();
    }

    public void unfollow(User user) {
        followService.unFollow(Cache.getInstance().getCurrUserAuthToken(), user, new UnFollowObserver());
    }

    public void follow(User user) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), user, new followObserver());
    }

    public void postStatus(String post, String dateAndTime, List<String> urls, List<String> mentions) {
        statusService.postStatus(post, Cache.getInstance().getCurrUser(), dateAndTime, urls, mentions, new PostStatusObserver());
    }

    public void getFollowersCount(User user) {
        followService.getFollowersCount(Cache.getInstance().getCurrUserAuthToken(), user, new GetFollowersCountObserver());
    }

    public void getFollowingCount(User selectedUser) {
        followService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowingCountObserver());
    }

    public void isFollower(User user) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), user, new IsFollowerObserver());
    }

    public void logout() {
        loginService.logout(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }

    public class LogoutObserver extends BaseObserver implements SimpleNotificationObserver {

        public LogoutObserver() {
            super(view);
        }

        @Override
        public void handleSuccess() {
            view.logoutSuccessful();
        }

        @Override
        String getDescription() {
            return "logout";
        }
    }

    public class GetFollowingCountObserver extends BaseObserver implements CountNotificationObserver {

        public GetFollowingCountObserver() {
            super(view);
        }

        @Override
        public void handleSuccess(int count) {
            view.setFollowingCount(count);
        }

        @Override
        String getDescription() {
            return "following";
        }
    }

    public class GetFollowersCountObserver extends BaseObserver implements CountNotificationObserver {

        public GetFollowersCountObserver() {
            super(view);
        }

        @Override
        public void handleSuccess(int count) {
            view.setFollowersCount(count);
        }


        @Override
        String getDescription() {
            return "followers";
        }
    }

    public class PostStatusObserver extends BaseObserver implements SimpleNotificationObserver {

        public PostStatusObserver() {
            super(view);
        }

        @Override
        public void handleSuccess() {
            view.postSuccessful();
        }


        @Override
        String getDescription() {
            return "post status";
        }
    }

    public class followObserver extends BaseObserver implements SimpleNotificationObserver {

        public followObserver() {
            super(view);
        }

        @Override
        public void handleSuccess() {
            view.followSuccessful();
        }


        @Override
        String getDescription() {
            return "follow";
        }
    }

    public class UnFollowObserver extends BaseObserver implements SimpleNotificationObserver {

        public UnFollowObserver() {
            super(view);
        }

        @Override
        public void handleSuccess() {
            view.unFollowSuccessful();
        }


        @Override
        String getDescription() {
            return "unfollow";
        }
    }

    public class IsFollowerObserver extends BaseObserver implements IsFollowerNotificationObserver {

        public IsFollowerObserver() {
            super(view);
        }

        @Override
        public void handleSuccess(boolean value) {
            view.setIsFollower(value);
        }


        @Override
        String getDescription() {
            return "following relationship";
        }
    }
}
