package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetUserNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService {

    public void getUser(AuthToken authToken, String userAlias, GetUserNotificationObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(authToken,
                userAlias, new GetUserNotificationHandler(getUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }
}
