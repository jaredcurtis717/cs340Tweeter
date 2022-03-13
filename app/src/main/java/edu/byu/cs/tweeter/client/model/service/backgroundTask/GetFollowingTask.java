package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {
    private static final String URL_PATH = "/getfollowing";

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected void runTask() throws Exception {
        FollowingRequest request;
        if (getLastItem() == null){
            request = new FollowingRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), null);
        }
        else{
            request = new FollowingRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), getLastItem().getAlias());
        }
        FollowingResponse response = getServerFacade().getFollowees(request, URL_PATH);

        if (response.isSuccess()){
            items = response.getFollowees();
            hasMorePages = response.getHasMorePages();
            sendSuccessMessage();
        }
        else{
            sendFailedMessage(response.getMessage());
        }
    }

}
