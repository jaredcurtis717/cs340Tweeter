package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserEntryNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserEntryNotificationHandler extends BackgroundTaskHandler<UserEntryNotificationObserver> {
    public UserEntryNotificationHandler(UserEntryNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(UserEntryNotificationObserver observer, Bundle data) {
        User loggedInUser = (User) data.getSerializable(AuthenticateTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);

        observer.handleSuccess(loggedInUser, authToken);
    }
}
