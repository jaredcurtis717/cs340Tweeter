package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetUserNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserNotificationHandler extends BackgroundTaskHandler<GetUserNotificationObserver> {

    public GetUserNotificationHandler(GetUserNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(GetUserNotificationObserver observer, Bundle data) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.handleSuccess(user);
    }
}
