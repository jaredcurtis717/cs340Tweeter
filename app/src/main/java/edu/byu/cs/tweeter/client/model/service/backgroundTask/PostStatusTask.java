package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {
    private static final String URL_PATH = "/poststatus";

    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private final Status status;

    public PostStatusTask(AuthToken authToken, Status status, Handler messageHandler) {
        super(authToken, messageHandler);
        this.status = status;
    }

    @Override
    protected void runTask() throws Exception {
        PostStatusRequest request = new PostStatusRequest(getAuthToken(), status);
        Response response = getServerFacade().postStatus(request, URL_PATH);
        if (response.isSuccess()){
            sendSuccessMessage();
        }
        else{
            sendFailedMessage(response.getMessage());
        }
    }

}
