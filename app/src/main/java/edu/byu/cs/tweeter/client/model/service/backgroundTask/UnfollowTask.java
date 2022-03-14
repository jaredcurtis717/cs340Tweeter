package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {
    private static final String URL_PATH = "/unfollow";

    /**
     * The user that is being followed.
     */
    private final User followee;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void runTask() throws Exception {
        FollowRequest request = new FollowRequest(getAuthToken(), followee.getAlias());

        Response response = getServerFacade().follow(request, URL_PATH);

        if (response.isSuccess()){
            sendSuccessMessage();
        }
        else{
            sendFailedMessage(response.getMessage());
        }
    }


}
