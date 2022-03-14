package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.Response;


/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    private static String URL_PATH = "/follow";
    /**
     * The user that is being followed.
     */
    private final User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void runTask() throws Exception {
        TargetUserRequest request = new TargetUserRequest(getAuthToken(), followee.getAlias());

        Response response = getServerFacade().follow(request, URL_PATH);

        if (response.isSuccess()){
            sendSuccessMessage();
        }
        else{
            sendFailedMessage(response.getMessage());
        }
    }

}
