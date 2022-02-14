package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UserEntryNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserEntryNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LoginService extends Service {

    public void logout(AuthToken authToken, SimpleNotificationObserver logoutObserver) {
        LogoutTask logoutTask = new LogoutTask(authToken, new SimpleNotificationHandler(logoutObserver));
        runTask(logoutTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String image, UserEntryNotificationObserver registerObserver) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, image, new UserEntryNotificationHandler(registerObserver));

        runTask(registerTask);
    }

    public void login(String alias, String password, UserEntryNotificationObserver loginObserver) {
        LoginTask loginTask = new LoginTask(alias, password, new UserEntryNotificationHandler(loginObserver));
        runTask(loginTask);
    }

}
