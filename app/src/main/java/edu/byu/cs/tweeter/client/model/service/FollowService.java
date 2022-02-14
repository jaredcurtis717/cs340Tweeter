package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.CountNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.CountNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.IsFollowerNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service {


    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PagedNotificationObserver<User> getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PagedNotificationHandler<>(getFollowingObserver));
        runTask(getFollowingTask);
    }

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, PagedNotificationObserver<User> getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new PagedNotificationHandler<>(getFollowersObserver));
        runTask(getFollowersTask);
    }

    public void follow(AuthToken currUserAuthToken, User user, SimpleNotificationObserver followObserver) {
        FollowTask followTask = new FollowTask(currUserAuthToken, user, new SimpleNotificationHandler(followObserver));
        runTask(followTask);
    }

    public void unFollow(AuthToken authToken, User user, SimpleNotificationObserver unFollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                user, new SimpleNotificationHandler(unFollowObserver));
        runTask(unfollowTask);
    }

    public void getFollowersCount(AuthToken authToken, User user, CountNotificationObserver getFollowersCountObserver) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                user, new CountNotificationHandler(getFollowersCountObserver));
        runTask(followersCountTask);
    }

    public void getFollowingCount(AuthToken authToken, User user, CountNotificationObserver getFollowingCountObserver) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                user, new CountNotificationHandler(getFollowingCountObserver));
        runTask(followingCountTask);
    }

    public void isFollower(AuthToken authToken, User currUser, User selectedUser, IsFollowerNotificationObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                currUser, selectedUser, new IsFollowerNotificationHandler(isFollowerObserver));
        runTask(isFollowerTask);
    }


}
