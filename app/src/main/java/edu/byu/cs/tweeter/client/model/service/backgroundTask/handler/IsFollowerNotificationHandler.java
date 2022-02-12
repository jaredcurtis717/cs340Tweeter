package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.IsFollowerNotificationObserver;

public class IsFollowerNotificationHandler extends BackgroundTaskHandler<IsFollowerNotificationObserver> {
    public IsFollowerNotificationHandler(IsFollowerNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(IsFollowerNotificationObserver observer, Bundle data) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }
}
