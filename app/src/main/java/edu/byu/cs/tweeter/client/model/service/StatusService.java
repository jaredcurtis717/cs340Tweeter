package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Service {

    public void postStatus(Status newStatus, SimpleNotificationObserver postStatusObserver) {

        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new SimpleNotificationHandler(postStatusObserver));
        runTask(statusTask);
    }

    public void getFeed(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedNotificationObserver<Status> getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken,
                user, pageSize, lastStatus, new PagedNotificationHandler<Status>(getFeedObserver));
        runTask(getFeedTask);
    }

    public void getStory(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedNotificationObserver<Status> getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken,
                user, pageSize, lastStatus, new PagedNotificationHandler<Status>(getStoryObserver));
        runTask(getStoryTask);
    }
}
