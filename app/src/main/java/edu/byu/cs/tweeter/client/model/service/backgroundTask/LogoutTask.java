package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthTokenRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {
    private static final String URL_PATH = "logout";

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken, messageHandler);
    }

    @Override
    protected void runTask() throws Exception{
        AuthTokenRequest request = new AuthTokenRequest(getAuthToken());

        Response response = getServerFacade().logout(request, URL_PATH);

        if (response.isSuccess()){
            sendSuccessMessage();
        }
        else{
            sendFailedMessage(response.getMessage());
        }
    }
}
