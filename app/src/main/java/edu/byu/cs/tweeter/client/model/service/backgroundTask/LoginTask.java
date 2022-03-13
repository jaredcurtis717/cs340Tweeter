package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {
    private static final String URL_PATH = "/login";



    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected LoginResponse runAuthenticationTask() throws Exception {
        try {
            LoginRequest request = new LoginRequest(username, password);
            LoginResponse response = getServerFacade().login(request, URL_PATH);

            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
