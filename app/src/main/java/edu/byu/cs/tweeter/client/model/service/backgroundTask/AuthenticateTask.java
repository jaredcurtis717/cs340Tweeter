package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public abstract class AuthenticateTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";
    private static final String LOG_TAG = "AuthenticateTask";

    private User authenticatedUser;

    private AuthToken authToken;

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    protected final String username;

    /**
     * The user's password.
     */
    protected final String password;

    protected AuthenticateTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }


    @Override
    protected final void runTask() throws IOException {
        try{
            LoginResponse loginResult = runAuthenticationTask();

            if (loginResult.isSuccess()){
                authenticatedUser = loginResult.getUser();
                authToken = loginResult.getAuthToken();

                sendSuccessMessage();
            }
            else{
                sendFailedMessage(loginResult.getMessage());
            }

        } catch (Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            sendExceptionMessage(exception);
        }

    }

    protected abstract LoginResponse runAuthenticationTask() throws Exception;

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }
}
