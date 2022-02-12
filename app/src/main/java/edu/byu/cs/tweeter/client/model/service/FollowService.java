package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.CountNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.CountNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {


    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, GetFollowingObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PagedNotificationHandler<>(getFollowingObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, GetFollowersObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new PagedNotificationHandler<>(getFollowersObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void follow(AuthToken currUserAuthToken, User user, FollowObserver followObserver) {
        FollowTask followTask = new FollowTask(currUserAuthToken, user, new SimpleNotificationHandler(followObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public void unFollow(AuthToken authToken, User user, UnFollowObserver unFollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                user, new SimpleNotificationHandler(unFollowObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    public void getFollowersCount(AuthToken authToken, User user, GetFollowersCountObserver getFollowersCountObserver) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                user, new CountNotificationHandler(getFollowersCountObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followersCountTask);
    }

    public void getFollowingCount(AuthToken authToken, User user, GetFollowingCountObserver getFollowingCountObserver) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                user, new CountNotificationHandler(getFollowingCountObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followingCountTask);
    }

    public void isFollower(AuthToken authToken, User currUser, User selectedUser, IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                currUser, selectedUser, new IsFollowerHandler(isFollowerObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public interface GetFollowingObserver extends PagedNotificationObserver<User>{

    }

    public interface GetFollowersObserver extends PagedNotificationObserver<User> {
    }


    public interface FollowObserver extends SimpleNotificationObserver {
        void setFollowButtonEnabled(boolean value);
    }

    public interface UnFollowObserver extends SimpleNotificationObserver{
        void setFollowButtonEnabled(boolean value);
    }

    public interface GetFollowersCountObserver extends CountNotificationObserver {
    }


    public interface GetFollowingCountObserver extends CountNotificationObserver{
    }

    public interface IsFollowerObserver {
        void handleSuccess(boolean value);

        void handleFailure(String message);

        void handleException(Exception exception);
    }

}
