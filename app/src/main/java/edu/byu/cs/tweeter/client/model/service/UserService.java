package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetUserNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService extends Service {

    public void getUser(AuthToken authToken, String userAlias, GetUserNotificationObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(authToken,
                userAlias, new GetUserNotificationHandler(getUserObserver));
        runTask(getUserTask);
    }
}
